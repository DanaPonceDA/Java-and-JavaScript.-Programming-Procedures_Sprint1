/**
 * @jest-environment jsdom
 */
import $ from "jquery";
import { setupWebSocket, setupOfferForm } from "../script.js";

global.$ = $;

describe("Módulo de subastas (frontend)", () => {
  let mockSocket;

  beforeEach(() => {
    // Simula el DOM mínimo necesario
    document.body.innerHTML = `
      <div id="price-123">100</div>
      <form id="offer-form" action="/offers">
        <input type="text" name="itemId" value="123">
        <input type="text" name="offerPrice" value="150">
        <input type="email" name="offerUser" value="test@example.com">
      </form>
      <div id="offer-success" class="hidden"></div>
      <div id="offer-error" class="hidden"></div>
    `;

    // Mock del WebSocket
    mockSocket = {
      send: jest.fn(),
      close: jest.fn(),
      onmessage: null
    };

    global.WebSocket = jest.fn(() => mockSocket);

    // Mock de jQuery AJAX
    $.ajax = jest.fn();
  });

  // --- WebSocket tests ---
  test("actualiza el precio correctamente cuando recibe un mensaje updatePrice", () => {
    setupWebSocket();

    const message = {
      type: "updatePrice",
      itemId: "123",
      price: 250.75
    };

    // Simular evento WebSocket
    mockSocket.onmessage({ data: JSON.stringify(message) });

    expect($("#price-123").text()).toBe("250.75");
  });

  test("ignora mensajes que no son updatePrice", () => {
    setupWebSocket();

    const originalText = $("#price-123").text();
    const message = { type: "otherType" };
    mockSocket.onmessage({ data: JSON.stringify(message) });

    expect($("#price-123").text()).toBe(originalText);
  });

  // --- Form submission tests ---
  test("muestra mensaje de éxito al enviar oferta correctamente", () => {
    setupOfferForm();

    $.ajax.mockImplementation(({ success }) => success());

    $("#offer-form").trigger("submit");

    expect($("#offer-success").hasClass("hidden")).toBe(false);
  });

  test("muestra mensaje de error al fallar la oferta", () => {
    setupOfferForm();

    $.ajax.mockImplementation(({ error }) => error({ responseText: "Error en oferta" }));

    $("#offer-form").trigger("submit");

    expect($("#offer-error").hasClass("hidden")).toBe(false);
    expect($("#offer-error").text()).toBe("Error en oferta");
  });

  test("limpia los campos del formulario después de enviar correctamente", () => {
    setupOfferForm();

    $.ajax.mockImplementation(({ success }) => success());

    $("#offer-form").trigger("submit");

    expect($("#offer-form input[type=text]").val()).toBe("");
    expect($("#offer-form input[type=email]").val()).toBe("");
  });
});
