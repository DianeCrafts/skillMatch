import { createRouter, createWebHistory } from "vue-router";

/* USER PAGES */
import LandingPage from "../views/LandingPage.vue";
import Login from "../views/Login.vue";
import Register from "../views/Register.vue";
import Dashboard from "../views/Dashboard.vue";
import UploadResume from "../views/UploadResume.vue";
import Jobs from "../views/Jobs.vue";
import JobDetails from "../views/JobDetails.vue";
import JobApplicants from "../views/JobApplicants.vue";
import ResumeForm from "@/views/ResumeForm.vue";
import ResumeView from "@/views/ResumeView.vue";
/* RECRUITER PAGES */
import RecruiterDashboard from "../views/RecruiterDashboard.vue";
import PostJob from "../views/PostJob.vue";
import EditJob from "../views/EditJob.vue";
import MyJobs from "../views/MyJobs.vue";
const routes = [
  /* PUBLIC */
  { path: "/", component: LandingPage },
  { path: "/login", component: Login },
  { path: "/register", component: Register },

  /* USER */
  { path: "/dashboard", component: Dashboard },
  { path: "/upload-resume", component: UploadResume },
  { path: "/resume-form", component: ResumeForm },
  { path: "/jobs", component: Jobs },
  { path: "/jobs/:id", component: JobDetails, props: true },
  { path: "/resume-view/:id", name: "ResumeView", component: ResumeView},

  /* RECRUITER */
  { path: "/recruiter-dashboard", component: RecruiterDashboard },
  { path: "/post-job", component: PostJob },
  { path: "/edit-job", component: EditJob },
  { path: "/recruiter/my-jobs", component: MyJobs },
  { path: "/recruiter/applicants/:jobId", component: JobApplicants, props: true },
  /* 404 fallback */
  { path: "/:pathMatch(.*)*", redirect: "/" }
];

export default createRouter({
  history: createWebHistory(),
  routes,
});
