import axios from "axios";

const applicationApi = axios.create({
  baseURL: "http://localhost:8083/api/applications",
});

applicationApi.interceptors.request.use(config => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default applicationApi;
