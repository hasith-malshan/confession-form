const loginForm = document.getElementById("loginForm");
const message = document.getElementById("message");

loginForm.addEventListener("submit", async function (e) {
  e.preventDefault();

  const email = document.getElementById("loginUsername").value;
  const password = document.getElementById("loginPassword").value;

  const loginData = {
    email: email,
    password: password
  };

  try {
    const response = await fetch("http://localhost:8080/api/auth/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(loginData)
    });

    if (!response.ok) {
      throw new Error("Login failed");
    }

    const data = await response.json();

    localStorage.setItem("token", data.token);

    message.textContent = "Login successful!";
    message.style.color = "green";

    setTimeout(() => {
      window.location.href = "../homepage/index.html";
    }, 1000);

  } catch (error) {
    message.textContent = "Invalid email or password";
    message.style.color = "red";
  }
});