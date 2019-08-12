<!doctype html>
<html lang="en">
<head>
    <#include "include/head.ftlh"/>
    <title>Welcome!</title>
</head>
<body>
    <#include "include/topNav.ftlh"/>

    <main role="main" class="container">
            <h3><#if pageData.name != "">
                    Welcome back, ${pageData.name}!
                <#else>
                    Hello.
                </#if>

            </h3>
            <p>It is ${pageData.month} ${pageData.dom}, ${pageData.year?c}.</p>
            <p>Your browser's user agent is:<br><code>${pageData.userAgent}</code></p>
    </main>

    <#include "include/scripts.ftlh"/>
</body>
</html>

