
  // Fetch messages and add them to the page.
  function fetchMerchants(merchantSearch){
    const url = '/search';
    fetch(url).then((response) => {
      return response.json();
    }).then((merchants) => {
      const merchantContainer = document.getElementById('message-container');
      if(merchants.length == 0){
        merchantContainer.innerHTML = '<p>There are no merchants yet.</p>';
      }
      else{
        merchantContainer.innerHTML = '';
      }
      merchants.sort( function(a,b){  //sort alphabetically
        if(a.name < b.name) { return -1; }
        if(a.name > b.name) { return 1; }
        return 0;
      });
      let count = 0;
      merchants.forEach((merchant) => {
       console.log(merchant.name+" "+merchantSearch);
       if( (merchant.name.toLowerCase().includes(merchantSearch.toLowerCase())) || (merchant.location.toLowerCase().includes(merchantSearch.toLowerCase())) || (merchantSearch=="") ) {
        const merchantDiv = buildMerchantDiv(merchant);

        const linkEl = document.createElement('a');
        linkEl.setAttribute('href', '/merchants.html?id='+ merchant.id)
        linkEl.setAttribute("style", "text-decoration: none; color: #000000");
        linkEl.appendChild(merchantDiv)

        merchantContainer.appendChild(linkEl);
        count++;
       }
      });
      if(count==0 && merchantSearch!=""){
        merchantContainer.innerHTML = '<p>Search did not match any merchant.</p>';
      }
    });
  }

  function buildMerchantDiv(merchant){
   const merchantNameH4 = document.createElement("h4");
   merchantNameH4.appendChild(document.createTextNode(merchant.name))

   const nameDiv = document.createElement('div');
   nameDiv.setAttribute("style", "display: inline-block;");
   nameDiv.appendChild(merchantNameH4)
   console.log(nameDiv)

   const distDiv = document.createElement('div');
   distDiv.classList.add('right-align');
   if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(function(position) {
      var pos = {
        lat: position.coords.latitude,
        lon: position.coords.longitude
      };
    var distance = getDistance(pos.lat,pos.lon,merchant.latitude,merchant.longitude);
    distDiv.appendChild(document.createTextNode(distance + " from you"));
    });
   } else {
     console.log("error");
   }

   const headerDiv = document.createElement('div');
   headerDiv.classList.add('message-header');
   headerDiv.classList.add('panel-heading');
   headerDiv.appendChild(nameDiv);
   headerDiv.appendChild(distDiv);

   const bodyDiv1 = document.createElement('div');
   bodyDiv1.classList.add('message-body');
   bodyDiv1.appendChild(document.createTextNode(merchant.cuisine + " Cuisine"));

   const bodyDiv3 = document.createElement('div');
   bodyDiv3.classList.add('message-body');
   bodyDiv3.appendChild(document.createTextNode(merchant.location));

   const bodyContainer = document.createElement('div');
   bodyContainer.classList.add('panel-body');
   bodyContainer.appendChild(bodyDiv1);
   bodyContainer.appendChild(bodyDiv3);

   const merchantDiv = document.createElement('div');
   merchantDiv.classList.add("message-div");
   merchantDiv.classList.add("panel");
   merchantDiv.classList.add("panel-default");
   merchantDiv.appendChild(headerDiv);
   merchantDiv.appendChild(bodyContainer);

   return merchantDiv;
  }

  // Fetch data and populate the UI of the page.
  function buildUI(merchant){
    fetchMerchants(merchant);
  }

  function foundLocation(position){
    curr.lat = position.coords.latitude;
    curr.lon = position.coords.longitude;
  }

  function getDistance(lat1,lon1,lat2,lon2) {
    console.log(lat1+" "+lon1+" "+lat2+" "+lon2);
    var R = 6371; // km (change this constant to get miles)
    var dLat = (lat2-lat1) * Math.PI / 180;
    var dLon = (lon2-lon1) * Math.PI / 180;
    var a = Math.sin(dLat/2) * Math.sin(dLat/2) +
      Math.cos(lat1 * Math.PI / 180 ) * Math.cos(lat2 * Math.PI / 180 ) *
      Math.sin(dLon/2) * Math.sin(dLon/2);
    var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    var d = R * c;
    console.log(d);
    if (d>1) return Math.round(d*10)/10+"km";
    else if (d<=1) return Math.round(d*1000)+"m";
    return d;
  }

  function getMerchantSearch(){
    var vars = {};
    vars["merchant"]="";
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
        value = value.replace("%27", "'");
        value = value.replace("%20", " ");
        vars[key] = value;
    });
    return vars;
  }
