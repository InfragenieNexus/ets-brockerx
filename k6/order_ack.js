import http from 'k6/http';
import {check, sleep} from 'k6';

export let options = {
    vus: 200,
    duration: '300s',
};

const testUser = {
    email: "test.test@hotmail.com",
    password: "test",
    symbols: ["AAPL", "GOOG"]
};

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

export function setup() {
    const loginUrl = "http://brockerx:8080/api/v1/user/login";
    const payload = JSON.stringify({
        email: testUser.email,
        password: testUser.password
    });

    const res = http.post(loginUrl, payload, {
        headers: {'Content-Type': 'application/json'}
    });

    check(res, {
        'login ok': (r) => r.status === 200 && r.json('token') !== undefined
    });

    return {token: res.json('token')};
}

export default function (data) {
    // Récupérer le token depuis setup()
    const authHeader = `Bearer ${data.token}`;

    const url = "http://brockerx:8080/api/order";
    const symbol = "TSLA";
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
