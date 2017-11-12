$(function () {
    doSearch("");
    init();
});

function init() {
    $("#term-input").keyup(function () {
        var term = $(this).val();
        doSearch(term);
    });
}

function doSearch(term) {
    console.log("Performing search for " + term);
    $.get("/api/search", {"term": term}, showList);
}

function showList(list) {
    console.log("displaying " + JSON.stringify(list));
    if (list.length === 0) {
        console.log("hiding list as there no data");
        w3.hide("#book-list");
        w3.show("#no-result")
    } else {
        w3.hide("#no-result")
        w3.show("#book-list");
        w3.displayObject("book-list", {"books": list});
    }
}