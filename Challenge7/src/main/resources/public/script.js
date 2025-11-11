import $ from "jquery";

// --- WebSocket handler ---
export function setupWebSocket() {
  const socket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/ws");

  socket.onmessage = function (event) {
    const message = JSON.parse(event.data);
    if (message.type === "updatePrice") {
      $("#price-" + message.itemId).text(message.price.toFixed(2));
    }
  };

  return socket;
}

// --- Form handler ---
export function setupOfferForm() {
  $("#offer-form").submit(function (event) {
    event.preventDefault();
    const form = $(this);
    const url = form.attr("action");
    const formData = form.serialize();

    $.ajax({
      type: "POST",
      url: url,
      data: formData,
      success: function () {
        $("#offer-success").removeClass("hidden");
        form.find("input[type=text], input[type=email]").val("");
        setTimeout(() => $("#offer-success").addClass("hidden"), 3000);
      },
      error: function (jqXHR) {
        $("#offer-error").text(jqXHR.responseText);
        $("#offer-error").removeClass("hidden");
        setTimeout(() => $("#offer-error").addClass("hidden"), 3000);
      }
    });
  });
}

// --- Inicialización ---
$(document).ready(function () {
  setupWebSocket();
  setupOfferForm();
});
