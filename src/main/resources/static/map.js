"use strict"

var singleHouseAddress = $('#houseAddress').text();
var singleHouseCity = $('#houseCity').text();
var singleHouseState = $('#houseState').text();
var singleHouseZipcode = $('#houseZipcode').text();
console.log(singleHouseAddress);

mapboxgl.accessToken = MAPBOX_ACCESS_TOKEN;
const map = new mapboxgl.Map({
    container: 'map', // container ID
    style: 'mapbox://styles/mapbox/streets-v11', // style URL
    center: [-98.4936, 29.4241], // starting position [lng, lat]
    zoom: 8.7 // starting zoom. This zoom covers 1604 loop
});

map.addControl(new mapboxgl.NavigationControl());

var marker = new mapboxgl.Marker({
    draggable: false,
    color: "red"
}).setLngLat([-98.4936, 29.4241])
    .addTo(map);

var popup = new mapboxgl.Popup()
    .setLngLat(marker.getLngLat())
    .setHTML("<h6>" + singleHouseAddress + "</h6>")
    .setMaxWidth("300px")
marker.setPopup(popup);

geocode(singleHouseAddress, MAPBOX_ACCESS_TOKEN).then(function (info) {
    console.log(info)
    var singleHouseMarker1 = {
        lng: info[0],
        lat: info[1]
    };
    marker.setLngLat(singleHouseMarker1);
    popup.setHTML("<h3>" + singleHouseAddress + "</h3>");
});


// GEOCODE FUNCTION
function geocode(search, token) {
    var baseUrl = 'https://api.mapbox.com';
    var endPoint = '/geocoding/v5/mapbox.places/';
    return fetch(baseUrl + endPoint + encodeURIComponent(search) + '.json' + "?" + 'access_token=' + token)
        .then(function (res) {
            return res.json();
            // to get all the data from the request, comment out the following three lines...
        }).then(function (data) {
            return data.features[0].center;
        });
}