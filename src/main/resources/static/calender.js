"use strict"


var events = [];
$('.event').each(function (i){
    var title, start, end;


    title = $(this).children().first().text();
    start = $(this).children('.event-start').first().text();
    end = $(this).children('.event-end').first().text();
    events.push({title, start, end})


});


document.addEventListener('DOMContentLoaded', function() {
    var calendarEl = document.getElementById('calendar');
    var calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        aspectRatio: 2,
        height: 550,
        eventSources: [

            // your event source
            {
                events,
                color: 'black',     // an option!
                textColor: 'yellow' // an option!
            }

            // any other event sources...

        ],
        eventClick: function(info) {
            alert('Event Address: ' + info.event.title + "\n" + ' Event Start: ' + info.event.start + "\n" + ' Event End: ' + info.event.end);


            // change the border color just for fun
            info.el.style.borderColor = 'red';
        }
    });
    calendar.render();
});

