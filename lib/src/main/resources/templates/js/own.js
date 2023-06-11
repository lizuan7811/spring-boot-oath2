let url = 'http://192.168.126.134/findhist/getStock';  // 替換為您的 API 網址

let stockDatas;

d3.json(`${url}`)
  .then(response => {
    // 在這裡處理回應
    console.log("Success");
    var myDiv = document.getElementById('append');
        // 替換 HTML 內容
    // myDiv.innerHTML = response[0]['transactVolume'];
    stockDatas=response;
    // console.log(response);
  })
  .catch(error => {
    // 在這裡處理錯誤
    console.error(error);
  });

var showData= document.getElementById("showData");
console.log(showData);
showData.addEventListener("click",function(){

  const svg=d3.select('#svg');

  const width=svg.attr('width');
  const height=svg.attr('height');
  const margin={top:10,right:30,bottom: 50,left:80};
  const innerWidth=width-margin.left-margin.right;
  const innerHeight=height-margin.top-margin.bottom;

  // console.log(stockDatas);
  const dateExtent = d3.extent(stockDatas, d => convertRocToDate(d.date));

  console.log(dateExtent);

  const xScale=d3.scaleLinear()
  .domain(dateExtent)
  .range([0,innerWidth]);

  const dateFormatter=d3.timeFormat("%Y-%m-%d");

  const yScale=d3.scaleBand()
  .domain(stockDatas.map(d=>d.transactVolume))
  .range([0,innerHeight]);

  const g=svg.append('g').attr('id','maingroup')
  .attr('transform',`translate(${margin.left},${margin.top})`);

  const yAxis=d3.axisLeft(yScale);
  g.append('g').call(yAxis);

  const xAxis=d3.axisBottom(xScale).tickFormat(dateFormatter);

  g.append('g').attr('transform',`translate(0,${innerHeight})`).call(xAxis);

  // const y=d3.scaleLinear().domain([0, d3.max(stockDatas, d => parseInt(d.transactVolume.replace(/,/g, "")))]).range([0,100]);

  // const rects=svg.selectAll('rect').data(stockDatas).enter().append('rect').attr('x',(d,i)=>{return i*12})
  // .attr('height',(d,i)=>{return y(parseInt(d.transactVolume.replace(/,/g, "")))}).attr('width',10).attr('fill','black');

},false);

function convertRocToDate(rocDateStr){
  const[rocYear,month,day]=rocDateStr.split('-');

  const year=parseInt(rocYear)+1911;
  const date=new Date(year,month-1,day);
  console.log(date);

  return date;
}