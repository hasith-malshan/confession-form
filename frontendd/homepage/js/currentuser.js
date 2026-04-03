export async function fetchCurrentUser() {
  const token = localStorage.getItem("token");

  if (!token) {
    return null;
  }

  try {
    const response = await fetch("http://localhost:8080/api/auth/me", {
      method: "GET",
      headers: {
        "Authorization": "Bearer " + token
      }
    });

    if (!response.ok) {
      return null;
    }

    return await response.json();

  } catch (error) {
    console.error("Error fetching current user:", error);
    return null;
  }
}