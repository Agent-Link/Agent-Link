"use strict"


var events = [];
$('.event').each(function (i){
    var title, start, end, id;

    id = $(this).children('.event-id').first().text();
    var url = "/events/" + id;
    title = $(this).children('.event-title').first().text();
    start = $(this).children('.event-start').first().text().toLocaleString('en-US');
    end = $(this).children('.event-end').first().text().toLocaleString('en-US');
    events.push({title, start, end, url})


});
console.log(events);

document.addEventListener('DOMContentLoaded', function() {

    var calendarEl = document.getElementById('calendar');
    var calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        aspectRatio: 4,
        height: 700,
        eventSources: [

            // your event source
            {
                events

            }

            // any other event sources...

        ],
        eventClick: function (info) {
            // alert('Event Address: ' + info.event.title + "\n" + ' Event Start: ' + info.event.start.toLocaleString('en-US') + "\n" + ' Event End: ' + info.event.end.toLocaleString('en-US'));
            var id = info.event.id
            // window.location.href = "/events/{info.event.id}";
            window.location.href = info.event.url;

            // change the border color just for fun
            info.el.style.borderColor = 'red';
        }
    });
    calendar.render();
});

// document.addEventListener('DOMContentLoaded', function() {
//     render();
// });
//
// document.getElementById("calendars").addEventListener("click", render);




