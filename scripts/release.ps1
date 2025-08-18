param(
    [Parameter(Mandatory = $true)]
    [string]$version
)

$ErrorActionPreference = "Stop"

$ProjectRoot = (Resolve-Path "$PSScriptRoot\..").Path
$TargetDir   = Join-Path $ProjectRoot "target"
$Artifacts   = @(
    "aether-profiler-$version.jar",
    "aether-profiler-$version-sources.jar",
    "aether-profiler-$version-javadoc.jar",
    "aether-profiler-$version.pom"
) | ForEach-Object { Join-Path $TargetDir $_ }

Push-Location $TargetDir
try {
    foreach ($f in $Artifacts) {
        if (-not (Test-Path $f)) {
            throw "Missing file: $f (expected in '$TargetDir')"
        }
    }

    # Generate checksums
    foreach ($f in $Artifacts) {
        foreach ($alg in @("MD5", "SHA1")) {
            $hash = (Get-FileHash $f -Algorithm $alg).Hash.ToLower()
            $outFile = "$f.$($alg.ToLower())"
            Set-Content -Path $outFile -Value $hash -NoNewline -Encoding ASCII
        }
    }

    # Verify checksums
    foreach ($f in $Artifacts) {
        $m1 = Get-Content "$f.md5"  -Raw
        $m2 = (Get-FileHash $f -Algorithm MD5).Hash.ToLower()
        if ($m1 -ne $m2) { throw "MD5 mismatch: $f" }

        $s1 = Get-Content "$f.sha1" -Raw
        $s2 = (Get-FileHash $f -Algorithm SHA1).Hash.ToLower()
        if ($s1 -ne $s2) { throw "SHA1 mismatch: $f" }
    }

    Write-Host "All checksums generated and verified successfully." -ForegroundColor Green

} finally {
    Pop-Location
}
