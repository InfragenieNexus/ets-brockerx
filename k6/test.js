import http from 'k6/http';
import {check, sleep} from 'k6';

export let options = {
    vus: 100,
    duration: '3000s',
};

export default function () {
    const url = 'http://brockerx:8080/api/v1/user/1';

    // Header Authorization exact comme dans ton curl
    const params = {
        headers: {
            'Authorization': 'Basic dGVzdC50ZXN0QGhvdG1haWwuY29tOnRlc3Q=',
            'Accept': 'application/json'
        }
    };

    let res = http.get(url, params);

    // Affiche le status pour déboguer
    console.log(`Status: ${res.status}`);

    check(res, {'status is 200': (r) => r.status === 200});

    sleep(1);
}
