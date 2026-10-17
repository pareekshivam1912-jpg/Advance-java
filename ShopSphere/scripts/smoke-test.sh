#!/usr/bin/env bash
# ShopSphere Web Application Smoke Test Suite for Linux / macOS / Git Bash
# Target: http://localhost:8080/shopsphere

BASE_URL="http://localhost:8080/shopsphere"
echo "====================================================="
echo "ShopSphere Web Application Smoke Test Suite"
echo "Target: ${BASE_URL}"
echo "====================================================="

endpoints=(
    "/:200:Homepage"
    "/products:200:Catalog Browse"
    "/product?id=1:200:Product Details"
    "/login:200:Login Page"
    "/register:200:Register Page"
    "/cart:302:Cart (Auth Redirect)"
    "/admin/dashboard:302:Admin Dashboard (Auth Redirect)"
    "/demo/:200:Syllabus Demo Hub"
    "/demo/jsp-elements.jsp:200:JSP Elements Demo"
    "/demo/el-demo.jsp:200:EL Scopes Demo"
    "/demo/jstl-xml.jsp:200:JSTL XML Demo"
    "/demo/jstl-sql.jsp:200:JSTL SQL Demo"
    "/demo/jndi-demo.jsp:200:JNDI DataSource Demo"
    "/demo/session-demo.jsp:200:Session Inspector Demo"
    "/lang?set=hi:302:i18n Hindi Switcher"
)

passed=0
failed=0

for item in "${endpoints[@]}"; do
    IFS=":" read -r path expected name <<< "$item"
    url="${BASE_URL}${path}"
    status=$(curl -o /dev/null -s -w "%{http_code}" "$url")

    if [ "$status" -eq "$expected" ] || ([ "$expected" -eq 302 ] && ([ "$status" -eq 302 ] || [ "$status" -eq 301 ])); then
        echo "[$status] GET $path ($name) ... PASS"
        ((passed++))
    else
        echo "[$status] GET $path ($name) ... FAIL (Expected $expected)"
        ((failed++))
    fi
done

echo "====================================================="
echo "Smoke Test Completed: $passed Passed, $failed Failed."
echo "====================================================="
