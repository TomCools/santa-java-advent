// Custom Christmas Icon
const treeIcon = L.icon({iconUrl: 'tree-marker.png', iconSize: [20, 20]})

const map = L.map('map', {doubleClickZoom: false}).setView([66.5039, 25.7294], 5);
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors',
}).addTo(map);


const linesGroup = L.layerGroup().addTo(map);

const visitsOnMap = [];
const solveButton = document.getElementById("solveButton");

// Allows you to manually add visits
map.on('click', function(e) {
    const { lat, lng } = e.latlng;
    addLocation(lat, lng);
});

// Add a location to the map and Javascript array
function addLocation(lat, lng) {
    visitsOnMap.push({
        id: visitsOnMap.length,
        location: {
            lat: lat,
            lng: lng
        }
    })
    // Add a marker at the clicked location
    L.marker([lat, lng], {icon: treeIcon}).addTo(map);
}

function solve() {
    linesGroup.clearLayers();
    solveButton.setAttribute("disabled", "disabled")
    solveButton.innerText = "Solving";
    fetch('http://localhost:8080/santa/plan', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json', // Specify the content type
        },
        body: JSON.stringify(visitsOnMap) // Convert your data to JSON string
    })
        .then(response => response.json()) // Parse JSON
        .then(data => {
            const homeLocation = data.santa.homeLocation;
            const locations = data.santa.visits.map(visit => visit.location);
            L.polyline([homeLocation, ...locations, homeLocation]).addTo(linesGroup);

            solveButton.removeAttribute("disabled")
            solveButton.innerText = "Solve";
            return data;
        })
        .catch(error => {
            console.error('Fetch error:', error);
        });
}