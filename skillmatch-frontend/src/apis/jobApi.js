import axios from "axios";

const jobApi = axios.create({
  baseURL: "http://localhost:8083/api",
});

jobApi.interceptors.request.use(config => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default jobApi;
