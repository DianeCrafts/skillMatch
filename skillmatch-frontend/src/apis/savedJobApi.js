import axios from "axios";

const savedJobApi = axios.create({
  baseURL: "http://localhost:8083/api/saved-jobs",
});

savedJobApi.interceptors.request.use(config => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default savedJobApi;
