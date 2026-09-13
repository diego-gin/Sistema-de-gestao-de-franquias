# =========================================================
# Script de apoio para testes manuais da Franquias API.
#
# Rode este script no início de QUALQUER sessão nova do PowerShell
# (depois de já ter os dados de exemplo criados pelo menos uma vez).
# Ele NÃO cria nada novo — só busca o que já existe no banco e
# preenche as variáveis $token, $headers, $categoria, $produto,
# $franqueadora, $franqueadoResp, $unidade e $fornecedor, prontas
# para usar no resto dos testes.
#
# Uso:
#   .\bootstrap-sessao.ps1
# =========================================================

[Console]::OutputEncoding = [System.Text.Encoding]::UTF8

Write-Host "Fazendo login..." -ForegroundColor Cyan
$resposta = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" `
    -Method Post -ContentType "application/json" `
    -Body '{"email":"admin@franquias.com","senha":"admin123"}'
$global:token = $resposta.token
$global:headers = @{ Authorization = "Bearer $token" }
Write-Host "Login OK. Usuario: $($resposta.nome) ($($resposta.perfil))" -ForegroundColor Green

Write-Host "Buscando categoria existente..." -ForegroundColor Cyan
$categorias = Invoke-RestMethod -Uri "http://localhost:8080/api/categorias" -Headers $headers
$global:categoria = $categorias | Select-Object -First 1
if ($null -eq $categoria) {
    Write-Host "Nenhuma categoria encontrada. Rode o cadastro manualmente (Parte 5)." -ForegroundColor Yellow
} else {
    Write-Host "Categoria: $($categoria.nome) (id $($categoria.id))" -ForegroundColor Green
}

Write-Host "Buscando produto existente..." -ForegroundColor Cyan
$produtos = Invoke-RestMethod -Uri "http://localhost:8080/api/produtos" -Headers $headers
$global:produto = $produtos | Select-Object -First 1
if ($null -eq $produto) {
    Write-Host "Nenhum produto encontrado. Rode o cadastro manualmente (Parte 5)." -ForegroundColor Yellow
} else {
    Write-Host "Produto: $($produto.nome) (id $($produto.id))" -ForegroundColor Green
}

Write-Host "Buscando franqueadora existente..." -ForegroundColor Cyan
$franqueadoras = Invoke-RestMethod -Uri "http://localhost:8080/api/franqueadoras" -Headers $headers
$global:franqueadora = $franqueadoras | Select-Object -First 1
if ($null -eq $franqueadora) {
    Write-Host "Nenhuma franqueadora encontrada. Rode o cadastro manualmente (Parte 4)." -ForegroundColor Yellow
} else {
    Write-Host "Franqueadora: $($franqueadora.razaoSocial) (id $($franqueadora.id))" -ForegroundColor Green
}

Write-Host "Buscando franqueado existente..." -ForegroundColor Cyan
$franqueados = Invoke-RestMethod -Uri "http://localhost:8080/api/franqueados" -Headers $headers
$global:franqueadoResp = $franqueados | Select-Object -First 1
if ($null -eq $franqueadoResp) {
    Write-Host "Nenhum franqueado encontrado. Rode o cadastro manualmente (Parte 4)." -ForegroundColor Yellow
} else {
    Write-Host "Franqueado: $($franqueadoResp.nome) (id $($franqueadoResp.id))" -ForegroundColor Green
}

Write-Host "Buscando unidade existente..." -ForegroundColor Cyan
$unidades = Invoke-RestMethod -Uri "http://localhost:8080/api/unidades" -Headers $headers
$global:unidade = $unidades | Select-Object -First 1
if ($null -eq $unidade) {
    Write-Host "Nenhuma unidade encontrada. Rode o cadastro manualmente (Parte 4)." -ForegroundColor Yellow
} else {
    Write-Host "Unidade: $($unidade.nomeFantasia) (id $($unidade.id))" -ForegroundColor Green
}

Write-Host "Buscando fornecedor existente..." -ForegroundColor Cyan
$fornecedores = Invoke-RestMethod -Uri "http://localhost:8080/api/fornecedores" -Headers $headers
$global:fornecedor = $fornecedores | Select-Object -First 1
if ($null -eq $fornecedor) {
    Write-Host "Nenhum fornecedor encontrado. Rode o cadastro manualmente (Parte 7)." -ForegroundColor Yellow
} else {
    Write-Host "Fornecedor: $($fornecedor.nome) (id $($fornecedor.id))" -ForegroundColor Green
}

Write-Host ""
Write-Host "Pronto! Variaveis disponiveis: `$token, `$headers, `$categoria, `$produto, `$franqueadora, `$franqueadoResp, `$unidade, `$fornecedor" -ForegroundColor Magenta
