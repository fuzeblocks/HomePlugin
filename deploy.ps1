param(
    [string]$server = $(Read-Host "Nom du serveur (ex: 1.21.8, 1.14, etc.)")
)

# Chemin vers le fichier de configuration
$configFile = "C:\Tools\HomePluginPush\$server.txt"
if (!(Test-Path $configFile)) {
    Write-Host "[ERREUR] Fichier de configuration introuvable : $configFile"
    exit 1
}

# Chargement et parsing du fichier config
$creds = Get-Content $configFile | ForEach-Object {
    $parts = $_ -split "=", 2
    [PSCustomObject]@{
        Key = $parts[0].Trim()
        Value = $parts[1].Trim().Trim('"') # Enlève les guillemets si présents
    }
}

# Conversion en dictionnaire
$dict = @{}
foreach ($pair in $creds) {
    $dict[$pair.Key] = $pair.Value
}

# Variables SSH
$sshHost = $dict["host"]
$sshPort = $dict["port"]
$sshUser = $dict["user"]

if ($dict.ContainsKey("password_file")) {
    $sshPasswordFile = $dict["password_file"]
    if (!(Test-Path $sshPasswordFile)) {
        Write-Host "[ERREUR] Fichier mot de passe chiffré introuvable : $sshPasswordFile"
        exit 1
    }
    $securePass = Get-Content $sshPasswordFile | ConvertTo-SecureString
    $sshPass = [System.Runtime.InteropServices.Marshal]::PtrToStringAuto(
        [System.Runtime.InteropServices.Marshal]::SecureStringToBSTR($securePass)
    )
} else {
    $sshPass = $dict["pass"]
}

$sshRemotePath = $dict["remote_path"]

# Vérification des variables
if (-not $sshHost -or -not $sshPort -or -not $sshUser -or -not $sshPass) {
    Write-Host "[ERREUR] Une ou plusieurs informations SSH sont manquantes :"
    Write-Host "Host: $sshHost"
    Write-Host "Port: $sshPort"
    Write-Host "User: $sshUser"
    Write-Host "Pass: $sshPass"
    exit 1
}

# Récupération du .jar le plus récent
$localJar = Get-ChildItem "./target/" -Filter "*.jar" | Sort-Object LastWriteTime -Descending | Select-Object -First 1
if (-not $localJar -or -not (Test-Path $localJar.FullName)) {
    Write-Host "[ERREUR] Aucun fichier .jar trouvé dans ./target/ ou chemin invalide"
    exit 1
}

# Génération du script PSFTP
$scriptPath = "./psftp_script.txt"
@"
lcd `"$($localJar.Directory.FullName)`"
cd $sshRemotePath
put $($localJar.Name)
bye
"@ | Out-File -Encoding ASCII $scriptPath

# Debug
Write-Host "`n[INFO] Déploiement vers : ${sshUser}@${sshHost}:${sshPort}"
Write-Host "[INFO] Répertoire distant : ${sshRemotePath}"
Write-Host "[INFO] Commande PSFTP : .\psftp.exe $sshHost -P $sshPort -l $sshUser -pw **** -b $scriptPath`n"

# Lancement de PSFTP
$arguments = @(
    $sshHost,
    "-P", $sshPort,
    "-l", $sshUser,
    "-pw", $sshPass,
    "-b", $scriptPath
)

Start-Process -Wait -NoNewWindow -FilePath "./psftp.exe" -ArgumentList $arguments

# Nettoyage
Remove-Item $scriptPath
Write-Host "`n[SUCCESS] Déploiement terminé sur '$server'."
