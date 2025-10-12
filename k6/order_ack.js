import http from 'k6/http';
import {check, sleep} from 'k6';

export let options = {
    vus: 10,              // un seul thread (il exécute en boucle les échanges)
    duration: '300s',    // 5 minutes
};

// Définition fixe des utilisateurs
const testUser = {
    email: "test.test@hotmail.com",
    auth: "Basic dGVzdC50ZXN0QGhvdG1haWwuY29tOnRlc3Q=",
    symbols: ["AAPL", "GOOG"]
};

const bobUser = {
    email: "bob@example.com",
    auth: "Basic Ym9iQGV4YW1wbGUuY29tOnRlc3Q=",
    symbols: ["AAPL", "TSLA"]
};

// Petite fonction utilitaire pour créer un ordre
function createOrder(user, side, symbol, price, quantity, type = "LIMIT", timeInForce = "GTC") {
    return {
        symbol,
        side,
        type,
        quantity,
        price,
        timeInForce,
        emailUser: user.email
    };
}

export default function () {
    const url = "http://brockerx:8080/api/order";

    const symbol = "AAPL"; // alterne symboles pour variation
    const price = 150;
    const quantity = 5;

    const sellOrder = createOrder(testUser, "BUY", symbol, price, quantity);
    const sellRes = http.post(url, JSON.stringify(sellOrder), {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': testUser.auth,
            'Accept': 'application/json',
            'Idempotency-Key': `${Math.random()}`
        }
    });
    check(sellRes, {'SELL ok': (r) => r.status === 200 || r.status === 400});
}
