/**
 * 
 */

var getName = function(){
  			return document.getElementById("name").value;
  		};
  		var getEmail = function(){
  			return document.getElementById("email").value;
  		};
  		
  		var getPassword = function(){
  			return document.getElementById("password").value;
  		};

  		var newUserProvider = function(){
  			return {"request":'{"email":"' + getEmail() + '", "name":"' + getName() + '", "password":"' + getPassword() + '"}',
  	  				"result":"undefined",
  	  				"successHandler": function(data){
  	  					this.result = data.status;
  	  				}};
  		}
  		
var sendRegisterRequest = function(testClient){
  			$.ajax({
  	  			type:'POST',
  	  			url: '/roboPoker-1.0-SNAPSHOT/rest/register',
  	  			contentType:'application/json',
  	  			data: testClient.request,
  	  			success:testClient.successHandler,
  	  			dataType:'json'
  	  		},1000);
  		};