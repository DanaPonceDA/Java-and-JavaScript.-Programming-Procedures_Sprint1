$(document).ready(function() {

    var socket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/ws");

    socket.onmessage = function(event) {
      var message = JSON.parse(event.data);
      if (message.type === "updatePrice") {
        $("#price-" + message.itemId).text(message.price.toFixed(2));
      }
    };

    $("#offer-form").submit(function(event) {
      event.preventDefault();
      var form = $(this);
      var url = form.attr("action");
      var formData = form.serialize();
      $.ajax({
        type: "POST",
        url: url,
        data: formData,
        success: function(response) {
          $("#offer-success").removeClass("hidden");
          form.find("input[type=text], input[type=email]").val("");
          setTimeout(function() {
            $("#offer-success").addClass("hidden");
          }, 3000);
        },
        error: function(jqXHR, textStatus, errorThrown) {
          $("#offer-error").text(jqXHR.responseText);
          $("#offer-error").removeClass("hidden");
          setTimeout(function() {
            $("#offer-error").addClass("hidden");
          }, 3000);
        }
      });
    });

  });
