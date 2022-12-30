<!-- the "home-page" element is passed as a parameter to VueComponent in the JavalinConfig file -->
<template id="home-page">
  <app-layout>
    <div class="row">
      <div class="col">
        <div class="card">
          <h5 class="card-header">Registered Users</h5>
          <div class="card-body">
            <h5 class="card-title">{{users.length}} Users</h5>
            <a href="/users" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
      <div class="col">
        <div class="card">
          <h5 class="card-header">Total Activities</h5>
          <div class="card-body">
            <h5 class="card-title">{{activities.length}} Activities</h5>
            <a href="/activities" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
      <div class="col">
        <div class="card">
          <h5 class="card-header">Online Consultation List</h5>
          <div class="card-body">
            <h5 class="card-title">{{onlineconsultationdetails.length}} Consultations</h5>
            <a href="/onlineconsultationdetails" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
    </div>

  </app-layout>
</template>

<script>
Vue.component('home-page',
    {
      template: "#home-page",
      data: () => ({
        users: [],
        activities: [],
        onlineconsultationdetails: []
      }),
      created() {
        axios.get("/api/users")
            .then(res => this.users = res.data)
            .catch(() => alert("Error while fetching users"));
        axios.get("/api/activities")
            .then(res => this.activities = res.data)
            .catch(() => alert("Error while fetching activities"));
        axios.get("/api/onlineconsultationdetails")
            .then(res => this.onlineconsultationdetails = res.data)
            .catch(() => alert("Error while fetching online consultation details"));
      }
    });
</script>


<style>

.row {
  margin-right: -30px;
  margin-left: -30px;
  padding-bottom: 10px;
  padding-top: 10px;
}
.btn-primary {
  color: #fff;
  background-color: #497790;
  border-color: #53a0aa;
}
</style>