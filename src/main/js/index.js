
var current = d3.select("#current");
var calendar = d3.select("#calendar");
var chart = d3.select("#chart");
var date = new Date();

var localeDefinition = {
    "dateTime": "%A, %e %B %Y г. %X",
    "date": "%d.%m.%Y",
    "time": "%H:%M:%S",
    "periods": ["AM", "PM"],
    "days": ["воскресенье", "понедельник", "вторник", "среда", "четверг", "пятница", "суббота"],
    "shortDays": ["вс", "пн", "вт", "ср", "чт", "пт", "сб"],
    "months": ["января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа", "сентября", "октября", "ноября", "декабря"],
    "shortMonths": ["янв", "фев", "мар", "апр", "май", "июн", "июл", "авг", "сен", "окт", "ноя", "дек"]
};

var locale = d3.timeFormatLocale(localeDefinition);
var formatMillisecond = locale.format("%H:%M"),
    formatSecond = locale.format("%H:%M"),
    formatMinute = locale.format("%H:%M"),
    formatHour = locale.format("%H:%M"),
    formatDay = locale.format("%H:%M"),
    formatWeek = locale.format("%H:%M"),
    formatMonth = locale.format("%H:%M"),
    formatYear = locale.format("%H:%M");

function formatDate(date) {
    var day = date.getDate();
    var monthIndex = date.getMonth();
    var year = date.getFullYear();
    return day + ' ' + localeDefinition["months"][monthIndex] + ' ' + year;
}

function formatTime(date) {
    var hour = date.getHours();
    var minute = date.getMinutes();
    return formatDate(date) + ' ' + hour + ':' + ('0' + minute).slice(-2);
}

var dateRange = function(date) {
    date.setHours(0, 0, 0, 0);
    var nextDate = new Date(date);
    nextDate.setDate(date.getDate()+1);
    return [date, nextDate];
};

// current
current.append("h3").text(formatTime(date));
current.append("h4").attr("style", "margin: 0").text("Tемпература: 20 C");
current.append("h4").attr("style", "margin: 0").text("Влажность: 40%");

// chart
var margin = {top: 20, right: 20, bottom: 20, left: 20};

var timeScale = d3.scaleTime().domain(dateRange(date));
var timeAxis = d3.axisBottom().scale(timeScale).tickFormat(function multiFormat(date) {
    return (d3.timeSecond(date) < date ? formatMillisecond
        : d3.timeMinute(date) < date ? formatSecond
            : d3.timeHour(date) < date ? formatMinute
                : d3.timeDay(date) < date ? formatHour
                    : d3.timeMonth(date) < date ? (d3.timeWeek(date) < date ? formatDay : formatWeek)
                        : d3.timeYear(date) < date ? formatMonth
                            : formatYear)(date)
});

var tempScale = d3.scaleLinear().domain([-20, 40]);
var tempAxis = d3.axisLeft().scale(tempScale).tickFormat(function(d){return d + " °C"});

var humScale = d3.scaleLinear().domain([0, 100]);
var humAxis = d3.axisRight().scale(humScale).tickFormat(function(d){return d + "%"});

var svg = chart.append("svg").attr("width", "100%").attr("height", "100%");
svg.append("text").attr("class", "title");
svg.append("g").attr("class", "timeAxis");
svg.append("g").attr("class", "tempAxis");
svg.append("g").attr("class", "humAxis");

d3.select(".title").datum(date)
    .attr("fill", "black")
    .attr("font-size", "30")
    .attr("font-family", "serif")
    .attr("text-anchor", "middle")
    .text(formatDate);

d3.select(".tempAxis").append("text").attr("class", "tempLabel").attr("transform", "rotate(-90)")
    .attr("fill", "black")
    .attr("font-size", "10")
    .attr("font-family", "sans-serif")
    .attr("text-anchor", "end").text("Температура");

d3.select(".humAxis").append("text").attr("class", "humLabel").attr("transform", "rotate(-90)")
    .attr("fill", "black")
    .attr("font-size", "10")
    .attr("font-family", "sans-serif")
    .attr("text-anchor", "end").text("Влажность");

var resize = function() {
    var width = chart._groups[0][0].clientWidth - margin.left - margin.right,
        height = chart._groups[0][0].clientHeight - margin.top - margin.bottom;

    timeScale.range([0, width - 80]);
    tempScale.range([height - 40, 0]);
    humScale.range([height - 40, 0]);

    d3.select(".title").attr("transform", "translate(" + (width/2)  + ", 40)");
    d3.select(".timeAxis").attr("transform", "translate(40, " + (height - 20)  + ")").call(timeAxis);
    d3.select(".tempAxis").attr("transform", "translate(40, 20)").call(tempAxis);
    d3.select(".humAxis").attr("transform", "translate(" + (width - 40)  + ", 20)").call(humAxis);
    d3.select(".tempLabel").attr("x", 0).attr("y", 0).attr("dy", "12");
    d3.select(".humLabel").attr("x", 0).attr("y", 0).attr("dy", "-4");
};

window.addEventListener("resize", resize);
resize();

// calendar
c = calendar.append("div")
    .attr("class", "classic-theme");
var myCalendar = jsCalendar.new({
    target : c._groups[0][0],
    date : date,
    navigator : true,
    navigatorPosition : "right",
    zeroFill : true,
    monthFormat : "month YYYY",
    firstDayOfTheWeek: 2,
    dayFormat: "D",
    language: "ru"
});

myCalendar.onDateClick(function(event, d) {
    myCalendar.clearselect();
    myCalendar.select(d);

    timeScale.domain(dateRange(d));
    d3.select(".timeAxis").call(timeAxis);
    d3.select(".title").datum(d).text(formatDate);
});