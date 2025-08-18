param(
    [Parameter(Mandatory = $true)]
    [string]$Version,                           # e.g. 1.1.0
    [string]$GroupId    = "de.splatgames.aether",
    [string]$ArtifactId = "aether-profiler",
    [string]$TargetDir                          # optional; defaults to <project>/target
)

$ErrorActionPreference = "Stop"

# Resolve project + target directories
$ProjectRoot = (Resolve-Path "$PSScriptRoot\..").Path
if (-not $TargetDir -or $TargetDir.Trim() -eq "") {
    $TargetDir = Join-Path $ProjectRoot "target"
} else {
    try { $TargetDir = (Resolve-Path $TargetDir -ErrorAction Stop).Path }
    catch {
        $maybe = Join-Path $ProjectRoot $TargetDir
        if (Test-Path $maybe) { $TargetDir = (Resolve-Path $maybe).Path }
    }
}
if (-not (Test-Path $TargetDir)) { throw "Target directory not found: '$TargetDir'" }

# Filenames we expect (already built by Maven)
$base      = "$ArtifactId-$Version"
$relFiles  = @("$base.pom", "$base.jar", "$base-sources.jar", "$base-javadoc.jar")
$srcFiles  = $relFiles | ForEach-Object { Join-Path $TargetDir $_ }

# Ensure artifacts exist
foreach ($f in $srcFiles) {
    if (-not (Test-Path $f)) { throw "Missing file: $(Split-Path $f -Leaf) (expected in '$TargetDir')" }
}

# Build Maven repository layout: de/splatgames/aether/aether-profiler/1.1.0
$groupPath   = ($GroupId -replace '\.','\')                       # e.g. de\splatgames\aether
$repoRoot    = Join-Path $TargetDir $groupPath                    # target\de\splatgames\aether
$artifactDir = Join-Path $repoRoot $ArtifactId                    # ...\aether-profiler
$versionDir  = Join-Path $artifactDir $Version                    # ...\1.1.0

# Clean and create destination directory
if (Test-Path $artifactDir) { Remove-Item $artifactDir -Recurse -Force }
New-Item -ItemType Directory -Force -Path $versionDir | Out-Null

# Collect artifacts + existing sidecar files (.asc/.md5/.sha1) IF they exist
$assets = @()
foreach ($f in $srcFiles) {
    $assets += $f
    foreach ($ext in @(".asc",".md5",".sha1")) {
        $p = "$f$ext"
        if (Test-Path $p) { $assets += $p }
    }
}

# Copy into Maven layout
$assets | Sort-Object -Unique | ForEach-Object {
    Copy-Item $_ -Destination $versionDir -Force
}

# Create ZIP at target\<artifact>-<version>-bundle.zip
$zipPath = Join-Path $TargetDir "$base-bundle.zip"
if (Test-Path $zipPath) { Remove-Item $zipPath -Force }

# IMPORTANT: zip starting at the TOP group folder so 'de/...' appears in the archive
$topGroupFolder = ($GroupId -split '\.')[0]                        # "de"
$topGroupPath   = Join-Path $TargetDir $topGroupFolder             # target\de
Compress-Archive -Path $topGroupPath -DestinationPath $zipPath

# List ZIP entries for a quick check
Add-Type -AssemblyName System.IO.Compression.FileSystem
$zip = [System.IO.Compression.ZipFile]::OpenRead($zipPath)
$entries = $zip.Entries | Select-Object FullName, Length
$zip.Dispose()

Write-Host "`nOK - Central bundle created"
Write-Host "ZIP: $zipPath"
$entries | Format-Table
