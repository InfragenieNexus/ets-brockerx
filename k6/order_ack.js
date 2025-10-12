import http from 'k6/http';
import {check, sleep} from 'k6';

export let options = {
    vus: 50,
    duration: '300s',
};

// Définition fixe des utilisateurs
const testUser = {
    email: "test.test@hotmail.com",
    password: "test",   // mot de passe pour login
    symbols: ["AAPL", "GOOG"]
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

// Fonction pour récupérer le token JWT via login
function loginAndGetToken(user) {
    const loginUrl = "http://brockerx:8080/api/v1/user/login";
    const payload = JSON.stringify({
        email: user.email,
        password: user.password
    });

    const res = http.post(loginUrl, payload, {
        headers: {'Content-Type': 'application/json'}
    });

    check(res, {
        'login ok': (r) => r.status === 200 && r.json('token') !== undefined
    });

    return res.json('token'); // récupère le token JWT
}

export default function () {
    // 1️⃣ Login pour récupérer le token
    const token = loginAndGetToken(testUser);
    const authHeader = `Bearer ${token}`;

    // 2️⃣ Placer un ordre
    const url = "http://brockerx:8080/api/order";
    const symbol = "AAPL";
    const price = 150;
    const quantity = 5;

    const sellOrder = createOrder(testUser, "BUY", symbol, price, quantity);
    const sellRes = http.post(url, JSON.stringify(sellOrder), {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': authHeader,
            'Accept': 'application/json',
            'Idempotency-Key': `${Math.random()}`
        }
    });

    check(sellRes, {
        'SELL ok': (r) => r.status === 200 || r.status === 400
    });

    sleep(1);
}
