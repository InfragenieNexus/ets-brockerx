<h2>Scan this QR code with Google Authenticator</h2>
<img src="data:image/png;base64,${qrCode}" />
<form action="verify-totp" method="post">
    <input type="number" name="code" placeholder="Enter code" required/>
    <button type="submit">Verify</button>
</form>
