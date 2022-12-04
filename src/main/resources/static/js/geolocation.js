function initMap() {
    var brasil = {
        lat : -14.239183,
        lng : -51.913726
    };

    var map = new google.maps.Map(document.getElementById("map"), {
        center : brasil,
        scrollwheel : true,
        zoom : 4
    });

    for (index = 0; index < students.length; ++index) {
        var latitude = students[index].address.coordinates[0];
        var longitude = students[index].address.coordinates[1];
        var coordinates = {
            lat : latitude,
            lng : longitude
        };

        var marker = new google.maps.Marker({
            position : coordinates,
            map, map,
            label: students[index].name
        });
    }
}

window.initMap = initMap;
