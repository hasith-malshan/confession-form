const signupForm = document.getElementById("signupForm");
const message = document.getElementById("message");

signupForm.addEventListener("submit", async function (e) {
  e.preventDefault();

  const username = document.getElementById("username").value;
  const email = document.getElementById("email").value;
  const password = document.getElementById("password").value;

  const signupData = {
    username: username,
    email: email,
    password: password
  };

  try {
    const response = await fetch("http://localhost:8080/api/auth/signup", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(signupData)
    });

    if (!response.ok) {
      throw new Error("Signup failed");
    }

    const data = await response.json();

    message.textContent = data.message || "Signup successful!";
    message.style.color = "green";

    setTimeout(() => {
      window.location.href = "login.html";
    }, 1000);

  } catch (error) {
    message.textContent = "Signup failed. Try another email.";
    message.style.color = "red";
  }
});