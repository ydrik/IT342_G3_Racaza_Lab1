import axios from "axios";

const API_URL = "http://localhost:8080/api/auth/";

// Function to register a user
const register = (username, email, password) => {
  // TODO: Once Backend is ready, uncomment the axios line below
  // return axios.post(API_URL + "register", { username, email, password });
  
  // MOCK: Simulate success for now
  console.log("Mock Register:", { username, email, password });
  return Promise.resolve({ data: { message: "User registered successfully!" } });
};

// Function to login a user
const login = (username, password) => {
  // TODO: Once Backend is ready, uncomment the axios line below
  // return axios.post(API_URL + "login", { username, password })
  //   .then((response) => {
  //     if (response.data.token) {
  //       localStorage.setItem("user", JSON.stringify(response.data));
  //     }
  //     return response.data;
  //   });

  // MOCK: Simulate success for now
  console.log("Mock Login:", { username, password });
  const mockUser = { username, token: "fake-jwt-token-123" };
  localStorage.setItem("user", JSON.stringify(mockUser));
  return Promise.resolve(mockUser);
};

const logout = () => {
  localStorage.removeItem("user");
};

const getCurrentUser = () => {
  return JSON.parse(localStorage.getItem("user"));
};

const AuthService = {
  register,
  login,
  logout,
  getCurrentUser,
};

export default AuthService;