const checkStatus = (response) => {
  if (response.status >= 200 && response.status < 300) {
    return response
  } else {
    var error = new Error(response.statusText)
    error.response = response
    throw error
  }
}

const parseJSON = (response) => response.json()

const initMainPage = () => {
  $(document).ready(function() {
    var $createOrderForm = $('#CreateAccount')
    var $accounts = $('#Accounts')

    $createOrderForm.on('submit', function(event) {
      event.preventDefault();

      var nameValue = $('#CreateAccount-name').val()

      fetch('/api/accounts', {
        method: 'POST',
        headers: { 'Content-type': 'application/json; charset=UTF-8' },
        body: JSON.stringify({ owner: nameValue })
      }).then(checkStatus)
        .then(parseJSON)
        .then(json => {
          $accounts.append("<a href='/account/" + json.id + "' >" + json.owner + "</a>")
        })
        .catch(error => { console.log('request failed', error) })
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

