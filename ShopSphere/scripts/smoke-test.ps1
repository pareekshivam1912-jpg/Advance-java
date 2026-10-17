<#
.SYNOPSIS
    ShopSphere Web Smoke Test Script for Windows PowerShell.
.DESCRIPTION
    Sends automated HTTP requests to verify all critical web endpoints,
    redirects, and academic demonstration pages.
#>

$baseUrl = "http://localhost:8080/shopsphere"
Write-Host "=====================================================" -ForegroundColor Cyan
Write-Host "ShopSphere Web Application Smoke Test Suite" -ForegroundColor Cyan
Write-Host "Target: $baseUrl" -ForegroundColor Cyan
Write-Host "=====================================================" -ForegroundColor Cyan

$endpoints = @(
    @{ Path = "/"; Expected = 200; Name = "Homepage" },
    @{ Path = "/products"; Expected = 200; Name = "Catalog Browse" },
    @{ Path = "/product?id=1"; Expected = 200; Name = "Product Details" },
    @{ Path = "/login"; Expected = 200; Name = "Login Page" },
    @{ Path = "/register"; Expected = 200; Name = "Register Page" },
    @{ Path = "/cart"; Expected = 302; Name = "Cart (Auth Redirect)" },
    @{ Path = "/admin/dashboard"; Expected = 302; Name = "Admin Dashboard (Auth Redirect)" },
    @{ Path = "/demo/"; Expected = 200; Name = "Syllabus Demo Hub" },
    @{ Path = "/demo/jsp-elements.jsp"; Expected = 200; Name = "JSP Elements Demo" },
    @{ Path = "/demo/el-demo.jsp"; Expected = 200; Name = "EL Scopes Demo" },
    @{ Path = "/demo/jstl-xml.jsp"; Expected = 200; Name = "JSTL XML Demo" },
    @{ Path = "/demo/jstl-sql.jsp"; Expected = 200; Name = "JSTL SQL Demo" },
    @{ Path = "/demo/jndi-demo.jsp"; Expected = 200; Name = "JNDI DataSource Demo" },
    @{ Path = "/demo/session-demo.jsp"; Expected = 200; Name = "Session Inspector Demo" },
    @{ Path = "/lang?set=hi"; Expected = 302; Name = "i18n Hindi Switcher" }
)

$passed = 0
$failed = 0

foreach ($ep in $endpoints) {
    $url = "$baseUrl$($ep.Path)"
    try {
        $response = Invoke-WebRequest -Uri $url -Method Get -MaximumRedirection 0 -UseBasicParsing -TimeoutSec 5 -ErrorAction SilentlyContinue
        $status = $response.StatusCode
    } catch {
        if ($_.Exception.Response) {
            $status = $_.Exception.Response.StatusCode.value__
        } else {
            $status = 0
        }
    }

    if ($status -eq $ep.Expected -or ($ep.Expected -eq 302 -and ($status -eq 302 -or $status -eq 301))) {
        Write-Host "[$status] GET $($ep.Path) ($($ep.Name)) ... PASS" -ForegroundColor Green
        $passed++
    } else {
        Write-Host "[$status] GET $($ep.Path) ($($ep.Name)) ... FAIL (Expected $($ep.Expected))" -ForegroundColor Red
        $failed++
    }
}

Write-Host "=====================================================" -ForegroundColor Cyan
Write-Host "Smoke Test Completed: $passed Passed, $failed Failed." -ForegroundColor $(if ($failed -eq 0) { "Green" } else { "Yellow" })
Write-Host "=====================================================" -ForegroundColor Cyan
