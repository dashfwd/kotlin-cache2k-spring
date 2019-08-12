<script>
    USE_CACHING=false;
    START = new Date().getTime();
    IS_LOADING=false;

    function useCaching() {
        return $('#useCaching').is(":checked");
    }

    function createTable() {

        var dataTable = $('#example').DataTable( {
            "ajax": {
                "url": "/api/people",
                "type": "POST",
                "data": function ( d ) {
                    d.useCaching = useCaching();
                }
            },
            "language": {
                "loadingRecords": "Please wait - loading..."
            },
            // "processing": true,
            "serverSide": true,
            "paging": false,
            "columns": [
                { "data": "firstName" },
                { "data": "lastName" },
                { "data": "birthYear" },
                { "data": "category" }
            ]
        });

        dataTable.on("preXhr", function() {
            console.log("preXhr....");
            IS_LOADING=true;
            START = new Date().getTime();
            setTimeout(function() {
                if (IS_LOADING) {
                    $("#loadStatus").text("");
                    $("#loaded").hide();
                    $("#loading").show();
                }
            },30);
        });
        dataTable.on("xhr", function() {
            IS_LOADING =false;
            $("#loading").hide();
            $("#loadStatus").html("Loading took <b>" + (new Date().getTime() - START) + " ms</b>");
            $("#loaded").show();
        });
    }

    $(function () {
        createTable();

        $("#reload").on("click", function() {
            var dataTable = $('#example').DataTable();
            dataTable.destroy();
            $("#example tbody").remove();
            createTable();
        });

        $("#useCaching").on("click", function() {
            if (!useCaching()) {
                $.ajax({
                    type: "POST",
                    url: "/api/clearPeopleCache",
                    context: this,
                    timeout: 6000,
                    success: function(data, textStatus, req) {
                        $("#loadStatus").html("<i class=\"fa fa-check-circle\" aria-hidden=\"true\"></i> Cache has been evicted.");
                    },
                    error: function() {
                        alert("Failed, maybe a network error?");
                    }
                });
            }
        });

    } );
</script>