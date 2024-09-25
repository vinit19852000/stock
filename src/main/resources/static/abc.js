

var per='';
var val='';
var gro='';
var pro='';
var per='';
var ent='';
var red='';

var appurl='https://stock-r362.onrender.com'
//var appurl='http://localhost:8081'
//'http://localhost:8081'
//https://stock-r362.onrender.com
var key=per+':'+val+':'+gro+':'+pro+':'+ent+':'+red;





 const dataMap = new Map();
 
 
 function comb(){
var resultContainer = document.getElementById('result');
     resultContainer.innerHTML='';
     
     var result='';
     var i=1;
      dataMap.forEach((value, key) => {
result += '<div style="font-size:25px; background-color:' + (i % 2 == 0 ? '#FFFDD0' : 'white') + '; margin-bottom: 10px;">' + i + ':' + key + '</div>';
result=result+'<br>';
i++;
    });
    
    
    resultContainer.innerHTML=result;
	
}
  window.onload = function() {
        fetchCombinationData(); // Replace with your actual function name
        getdate();
    };


function getdate(){
	            const apiUrl = appurl+'/stock/date'; // Adjust URL as needed


                var mydate=document.getElementById('mydate');
            fetch(apiUrl)
                .then(response => {
                    if (!response.ok) {

                return Promise.reject('No Result Found'); // Stop further processing
                    }
                    return response.text(); // Parse the response as JSON
                })
                .then(data => {
                    
                   mydate.innerHTML = '<div style="font-size:25px; background-color:red">' + data + '</div>';

                })
                .catch(error => {
                     console.error(error);

                });
}
        // Function to fetch data from the API and store it in the global map
        function fetchCombinationData() {
			
			  var resultContainer = document.getElementById('result');
    var spinner = document.getElementById('spinner');
    
    // Show spinner and clear previous results
    spinner.classList.remove('hidden');
    resultContainer.innerHTML = '';

            const apiUrl = appurl+'/stock/combination'; // Adjust URL as needed

            fetch(apiUrl)
                .then(response => {
                    if (!response.ok) {
                 spinner.classList.add('hidden'); // Hide the spinner
                resultContainer.innerHTML = 'No Result Found'; // Display error message
                return Promise.reject('No Result Found'); // Stop further processing
                    }
                    return response.json(); // Parse the response as JSON
                })
                .then(data => {
                    // Assuming the response is in the format you provided earlier
                    
                    for (const [key, value] of Object.entries(data)) {
						
						
						
                        dataMap.set(key, value); // Store each key-value pair in the global map
                    }
                    console.log('Data fetched and stored in map:', dataMap);
                                     spinner.classList.add('hidden'); // Hide the spinner
                                    
                   
                })
                .catch(error => {
                     console.error(error);
                                      spinner.classList.add('hidden'); // Hide the spinner
                });
        }
        
        
        
        function filter(){
			
			console.log('my key:'+key);
			const value = dataMap.get(key);
    var resultContainer = document.getElementById('result');
    
             resultContainer.innerHTML='';
     
if (value) {
    console.log("Value for key:", value);
       
            var i=1;
            // Iterate through the list of data and create clickable divs
            value.forEach(item => {
				
                var div = document.createElement('div');
                div.className = 'item'; // Add a class for styling
                div.innerText = i+'.'+item; // Set the text content to the item value
                div.style.fontSize='25px';
                if(i%2==0){
					                div.style.backgroundColor='#FFFDD0';
				}else{
					                div.style.backgroundColor='white';
				}


                // Append the new div to the result container
                resultContainer.appendChild(div);

            i++;
            });

} else {
    console.log("Key not found in the map.");
    resultContainer.innerHTML = 'No Result Found';
}
		}
function callme(){
	
	
	per=document.getElementById('performance').value;
	val=document.getElementById('valuation').value;
	gro=document.getElementById('growth').value;
	pro=document.getElementById('profitibility').value;
	ent=document.getElementById('entrypoint').value;
	red=document.getElementById('redflags').value;
	key=per+':'+val+':'+gro+':'+pro+':'+ent+':'+red;
	
	filter();
	
}

function gotopage(stock){
	
	console.log("go to called");
	fetch(appurl+'/stock/search?stock='+stock).then(response => {
            if (!response.ok) {
				                 spinner.classList.add('hidden'); // Hide the spinner
                resultContainer.innerHTML = 'No Result Found'; // Display error message
                return Promise.reject('No Result Found'); // Stop further processing
            }
            return  response.text(); // Parse the response as JSON
        })
        .then(data => {

                    console.log('mydata:'+data);
                    window.location.href=data;
        })
        .catch(error => {
			
			    console.log("error:"+error);
                 spinner.classList.add('hidden'); // Hide the spinner
                resultContainer.innerHTML = 'No Result Found'; // Display error message
                return Promise.reject('No Result Found'); // Stop further processing
        });
}


function get() {
    var resultContainer = document.getElementById('result');
    var spinner = document.getElementById('spinner');
    
    // Show spinner and clear previous results
    spinner.classList.remove('hidden');
    resultContainer.innerHTML = '';

    fetch(appurl+'/stock/map?key=' + key)
        .then(response => {
            if (!response.ok) {
                 spinner.classList.add('hidden'); // Hide the spinner
                resultContainer.innerHTML = 'No Result Found'; // Display error message
                return Promise.reject('No Result Found'); // Stop further processing
               
            }
            return response.json(); // Parse the response as JSON
        })
        .then(data => {
			
			 console.log('isnide');
            // Clear any previous results
             spinner.classList.add('hidden');

            resultContainer.innerHTML = ''; 
            
            var i=0;
            // Iterate through the list of data and create clickable divs
            data.forEach(item => {
				
                var div = document.createElement('div');
                div.className = 'item'; // Add a class for styling
                div.innerText = i+'.'+item; // Set the text content to the item value
                div.style.fontSize='25px';
                if(i%2==0){
					                div.style.backgroundColor='#FFFDD0';
				}else{
					                div.style.backgroundColor='white';
				}

                // Make the div clickable
                

                // Append the new div to the result container
                resultContainer.appendChild(div);
            i++;
            }
            );
        })
        .catch(error => {
			spinner.classList.add('hidden');
			 resultContainer.innerHTML = '<div><b>No Result Found</b></div>'; // Display error message
             return Promise.reject('No Result Found'); // Stop further processing
            console.error('There was a problem with the fetch operation:', error);
        });
}


 