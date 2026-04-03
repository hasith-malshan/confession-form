// ── Helpers ──

function showError(elementId, message) {
  const el = document.getElementById(elementId);
  if (!el) return;
  el.textContent = message;
  el.style.display = "block";
}

function hideError(elementId) {
  const el = document.getElementById(elementId);
  if (!el) return;
  el.style.display = "none";
}

// ── Login ──

const loginForm = document.getElementById("loginForm");

if (loginForm) {
  if (getToken()) {
    window.location.href = "../homepage/index.html";
  }

  loginForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    hideError("loginError");

    const username = document.getElementById("loginUsername").value.trim();
    const password = document.getElementById("loginPassword").value.trim();

    if (!username || !password) {
      showError("loginError", "Please fill in all fields.");
      return;
    }

    try {
      const data = await apiRequest("POST", "/auth/login", { username, password });
      setAuth(data);
      window.location.href = "../homepage/index.html";
    } catch (err) {
      showError("loginError", err.message || "Login failed. Please check your credentials.");
    }
  });
}

// ── Signup ──

const signupForm = document.getElementById("signupForm");

if (signupForm) {
  if (getToken()) {
    window.location.href = "../homepage/index.html";
  }

  signupForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    hideError("signupError");

    const body = {
      username: document.getElementById("username").value.trim(),
      fname: document.getElementById("fname").value.trim(),
      lname: document.getElementById("lname").value.trim(),
      email: document.getElementById("signupEmail").value.trim(),
      mobileNo: document.getElementById("mobileNo").value.trim(),
      password: document.getElementById("signupPassword").value,
    };
    const confirm = document.getElementById("confirmPassword").value;

    if (!body.username || !body.fname || !body.lname || !body.email || !body.mobileNo || !body.password || !confirm) {
      showError("signupError", "Please fill in all fields.");
      return;
    }

    if (body.password.length < 8) {
      showError("signupError", "Password must be at least 8 characters.");
      return;
    }

    if (body.password !== confirm) {
      showError("signupError", "Passwords do not match.");
      return;
    }

    try {
      const data = await apiRequest("POST", "/auth/register", body);
      setAuth(data);
      window.location.href = "../homepage/index.html";
    } catch (err) {
      showError("signupError", err.message || "Registration failed. Please try again.");
    }
  });
}

// ── Profile icon (for pages that have it) ──

const profileIcon = document.getElementById("profileIcon");
if (profileIcon) {
  const name = getUsername();
  profileIcon.textContent = name ? name.charAt(0).toUpperCase() : "?";
}
