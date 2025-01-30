// common.js
console.log("common.js is loaded");

async function fetchAndDisplayUsername() {
    console.log("fetchAndDisplayUsername called");
    const authToken = localStorage.getItem("token");
  
    // Check if the token exists
    if (!authToken) {
      console.error("Auth token not found in localStorage.");
      return;
    }
  
    try {
      const response = await fetch("/api/users/name", {
        method: "GET",
        headers: {
          "Authorization": `Bearer ${authToken}`, // Attach the token as Bearer
        },
      });
  
      if (response.ok) {
        const username = await response.text(); // Assuming the backend returns the username as plain text
        console.log("Fetched username:", username);
  
        // Display the username in the greeting message
        const greetingMessage = document.getElementById("greeting-message");
        greetingMessage.textContent = `Hello, ${username}`;
  
        // Optionally, hide the login/register buttons
        document.getElementById("auth-buttons").style.display = "none";
        document.getElementById("user-greeting").style.display = "block";
      } else {
        console.error(`Failed to fetch username`);
      }
    } catch (error) {
      console.error("Error fetching username:", error);
    }
  }

  function handleLogout() {
    // Remove the auth token from localStorage
    localStorage.removeItem("token");

    // Hide the greeting and show the login/register buttons again
    document.getElementById("user-greeting").style.display = "none";
    document.getElementById("auth-buttons").style.display = "block";

    // Redirect the user to the homepage or login page
    window.location.href = "/";  // Or any other page
}

// Event listener for logout button
document.getElementById("logout-button")?.addEventListener("click", handleLogout);