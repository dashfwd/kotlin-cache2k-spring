<!doctype html>
<html lang="en">
<head>
    <#include "include/head.ftlh"/>
    <title>Welcome!</title>
</head>
<body>
    <#include "include/topNav.ftlh"/>

    <main role="main" class="container">
        <div id="loading" style="background-color:yellow;">
            <div style="padding:10px">
                <i class="fa fa-refresh fa-spin fa-1x fa-fw" aria-hidden="true"></i> Loading
            </div>
        </div>
        <div id="loaded">
            <div style="padding:10px">
                <span id="loadStatus"></span>
            </div>
        </div>
        <div style="padding-top:5px">
            <button id="reload" type="button" class="btn btn-info">Reload</button> &nbsp;|&nbsp;
            Use caching? <input id="useCaching" type="checkbox">
        </div>

        <table id="example" class="display" style="width:100%">
            <thead>
            <tr>
                <th>First name</th>
                <th>Last name</th>
                <th>Birth Year</th>
                <th>Category</th>
            </tr>
            </thead>
        </table>
    </main>

    <#include "include/scripts.ftlh"/>
    <#include "include/dataTable.ftlh"/>
</body>
</html>

