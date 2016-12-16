const checkStatus = (response) => {
  if (response.status >= 200 && response.status < 300) {
    return response
  } else {
    var error = new Error(response.statusText)
    error.response = response
    throw error
  }
}

const parseJSON = (response) => {
  return response.json();


}


const loginAction = (json) => {
    $("#loginPanel").html("Hello, " + json.login + " <span id='unlog' class='glyphicon glyphicon-remove' aria-hidden='true'></span>").removeClass('panel-danger');
    $("#unlog").click(() => {
        localStorage.removeItem("Authorization");
    window.location= "/"
})

}

const initMainPage = () => {
  $(document).ready(function() {
    authentification();


    var $createOrderForm = $('#CreateAccount')
    var $accounts = $('#Accounts')

    $createOrderForm.on('submit', function(event) {
      event.preventDefault();

      var nameValue = $('#CreateAccount-name').val()
      var token = localStorage.getItem("Authorization");

      fetch('/api/accounts', {
        method: 'POST',
        headers: { 'Content-type': 'application/json; charset=UTF-8',  'Authorization': token },
        body: JSON.stringify({ owner: nameValue })
      }).then(checkStatus)
        .then(parseJSON)
        .then(json => {
          $accounts.append("<li><a href='/account/" + json.id + "' >" + json.owner + "</a></li>")
        })
        .catch(error => {
            console.log('request failed', error)
             if(error == "Error: Forbidden"){
                 $('#loginPanel').addClass('panel-danger');
             }
        })
    })
  })
}

const initAccountPage = () => {
  $(document).ready(function() {
    var accountIdValue = $('#Id').html()
    var $owner = $('#Owner')
    var $value = $('#Value')

    fetch('/api/accounts/' + accountIdValue)
        .then(checkStatus)
        .then(parseJSON)
        .then(json => {
      $owner.html(json.owner)
    $value.html(json.value)
  })
    .catch(error => { console.log('request failed', error) })
  })
}






const authentification = (todo) => {

  var token = localStorage.getItem("Authorization");
  if (token != null){

    fetch('/login',
        {
          headers: { 'Authorization': token }
        })
        .then(checkStatus)
        .then(parseJSON)
        .then(json => {

        loginAction(json)

  }).catch(error => { console.log('request failed', error) })

  }else {

    $("#loginForm").click(() => {

          var pwd = $('#lg_username').val();
          var login = $('#lg_password').val();

          fetch('/auth', {
            method: 'POST',
            headers: { 'Content-type': 'application/json; charset=UTF-8' },
            body: JSON.stringify({login: login, pwd: pwd})
          }).then(checkStatus)
              .then(parseJSON)
              .then(json => {
            localStorage.setItem("Authorization",json.jwtToken)
            return json.jwtToken;
          }).then((token) => {
                 fetch('/login',
                 {
                     headers: { 'Authorization': token }
                  }
                  ).then(checkStatus)
                   .then(parseJSON)
                   .then(json => {
          loginAction(json);
                    }
  )})
  .catch(error => {
              console.log('request failed', error)
          })
        })

  }

  }


