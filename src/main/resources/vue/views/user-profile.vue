<template id="user-profile">
  <app-layout>
    <div v-if="noUserFound">
      <p> We're sorry, we were not able to retrieve this user.</p>
      <p> View <a :href="'/users'">all users</a>.</p>
    </div>
    <div class="card bg-light mb-3" v-if="!noUserFound">
      <div class="card-header">
        <div class="row">
          <div class="col-6"> User Profile </div>
          <div class="col" align="right">
            <button rel="tooltip" title="Update"
                    class="btn btn-info btn-simple btn-link"
                    @click="updateUser()">
              <i class="far fa-save" aria-hidden="true"></i>
            </button>
            <button rel="tooltip" title="Delete"
                    class="btn btn-info btn-simple btn-link"
                    @click="deleteUser()">
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>
      <div class="card-body">
        <form>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-id">User ID</span>
            </div>
            <input type="number" class="form-control" v-model="user.id" name="id" readonly placeholder="Id"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-name">Name</span>
            </div>
            <input type="text" class="form-control" v-model="user.name" name="name" placeholder="Name"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-email">Email</span>
            </div>
            <input type="email" class="form-control" v-model="user.email" name="email" placeholder="Email"/>
          </div>
        </form>
      </div>
      <div class="card-footer text-left">
        <p  v-if="activities.length == 0"> No activities yet...</p>
        <p  v-if="activities.length > 0"> Activities so far...</p>
        <div class="card bg-light mb-3">
          <div class="card-header">
            <div class="row">
              <div class="col-6">
                Add Activity
              </div>
              <div class="col" align="right">
                <button rel="tooltip" title="Add"
                        class="btn btn-info btn-simple btn-link"
                        @click="hideForm =!hideForm">
                  <i class="fa fa-plus" aria-hidden="true"></i>
                </button>
              </div>
            </div>
          </div>
          <div class="card-body" :class="{ 'd-none': hideForm}">
            <form id="addActivity">
              <div class="input-group mb-3">
                <div class="input-group-prepend">
                  <span class="input-group-text" id="input-activity-description">Description</span>
                </div>
                <input type="text" class="form-control" v-model="formData.description" name="description" placeholder="Description"/>
              </div>
              <div class="input-group mb-3">
                <div class="input-group-prepend">
                  <span class="input-group-text" id="input-activity-duration">Duration</span>
                </div>
                <input type="number" class="form-control" v-model="formData.duration" name="duration" placeholder="Duration"/>
              </div>
              <div class="input-group mb-3">
                <div class="input-group-prepend">
                  <span class="input-group-text" id="input-activity-description">Calories</span>
                </div>
                <input type="text" class="form-control" v-model="formData.calories" name="calories" placeholder="Calories"/>
              </div>
              <div class="input-group mb-3">
                <div class="input-group-prepend">
                  <span class="input-group-text" id="input-activity-description">Date</span>
                </div>
                <input type="datetime-local" class="form-control" v-model="formData.date" name="date" placeholder="date"/>
              </div>
            </form>
            <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link" @click="addActivity()">Add Activity</button>
          </div>
        </div>
        <div class="list-group list-group-flush">
          <div class="list-group-item d-flex align-items-start"
               v-for="(activity,index) in activities" v-bind:key="index">
            <div class="mr-auto p-2">
              <span><a :href="`/activities/${activity.id}`"> {{ activity.description }} for {{ activity.duration }} minutes</a></span>
            </div>
            <div class="p2">
              <a :href="`/activities/${activity.id}`">
                <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link">
                  <i class="fa fa-pencil" aria-hidden="true"></i>
                </button>
              </a>
              <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link"
                      @click="deleteActivity(activity, index)">
                <i class="fas fa-trash" aria-hidden="true"></i>
              </button>
            </div>
          </div>
        </div>
      </div>

      <div class="card-footer text-left">
        <p  v-if="foodTrackerDetails.length == 0"> No FoodTracker Details yet...</p>
        <p  v-if="foodTrackerDetails.length > 0"> FoodTracker Details so far...</p>
        <div class="card bg-light mb-3">
          <div class="card-header">
            <div class="row">
              <div class="col-6">
                Add Food Intake details
              </div>
              <div class="col" align="right">
                <button rel="tooltip" title="Add"
                        class="btn btn-info btn-simple btn-link"
                        @click="hideForm =!hideForm">
                  <i class="fa fa-plus" aria-hidden="true"></i>
                </button>
              </div>
            </div>
          </div>
          <div class="card-body" :class="{ 'd-none': hideForm}">
            <form id="addFoodTracker">
              <div class="input-group mb-3">
                <div class="input-group-prepend">
                  <span class="input-group-text" id="input-foodtracker-meal">Meal</span>
                </div>
                <input type="text" class="form-control" v-model="formData.meal" name="meal" placeholder="Meal"/>
              </div>
              <div class="input-group mb-3">
                <div class="input-group-prepend">
                  <span class="input-group-text" id="input-foodtracker-caloriesintake">Calories Intake</span>
                </div>
                <input type="number" class="form-control" v-model="formData.caloriesintake" name="caloriesintake" placeholder="Calories Intake"/>
              </div>
              <div class="input-group mb-3">
                <div class="input-group-prepend">
                  <span class="input-group-text" id="input-foodtracker-date">Date</span>
                </div>
                <input type="datetime-local" class="form-control" v-model="formData.date" name="date" placeholder="Date"/>
              </div>
            </form>
            <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link" @click="addFoodTracker()">Add FoodTracker</button>
          </div>
        </div>
        <div class="list-group list-group-flush">
          <div class="list-group-item d-flex align-items-start"
               v-for="(foodtracker,index) in foodTrackerDetails" v-bind:key="index">
            <div class="mr-auto p-2">
              <span><a :href="`/foodtrackerdetails/${foodtracker.id}`"> {{ foodtracker.caloriesIntake }} calories for {{ foodtracker.meal }} </a></span>
            </div>
            <div class="p2">
              <a :href="`/foodtrackerdetails/${foodtracker.id}`">
                <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link">
                  <i class="fa fa-pencil" aria-hidden="true"></i>
                </button>
              </a>
              <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link"
                      @click="deleteFoodTracker(foodtracker, index)">
                <i class="fas fa-trash" aria-hidden="true"></i>
              </button>
            </div>
          </div>
        </div>
      </div>

      <div class="card-footer text-left">
        <p  v-if="goalSettingDetails.length == 0"> No Goal Setting Details yet...</p>
        <p  v-if="goalSettingDetails.length > 0"> Goal Setting Details so far...</p>
        <div class="card bg-light mb-3">
          <div class="card-header">
            <div class="row">
              <div class="col-6">
                Add Goals
              </div>
              <div class="col" align="right">
                <button rel="tooltip" title="Add"
                        class="btn btn-info btn-simple btn-link"
                        @click="hideForm =!hideForm">
                  <i class="fa fa-plus" aria-hidden="true"></i>
                </button>
              </div>
            </div>
          </div>
          <div class="card-body" :class="{ 'd-none': hideForm}">
            <form id="addGoalSetting">
              <div class="input-group mb-3">
                <div class="input-group-prepend">
                  <span class="input-group-text" id="input-goalsetting-month">Month</span>
                </div>
                <input type="month" class="form-control" v-model="formData.month" name="month" placeholder="Month"/>
              </div>
              <div class="input-group mb-3">
                <div class="input-group-prepend">
                  <span class="input-group-text" id="input-goalsetting-bodyfatpercentage">Body Fat Percentage</span>
                </div>
                <input type="number" class="form-control" v-model="formData.bodyfatpercentage" name="bodyfatpercentage" placeholder="Body Fat Percentage"/>
              </div>
              <div class="input-group mb-3">
                <div class="input-group-prepend">
                  <span class="input-group-text" id="input-goalsetting-kilosreducedpermonth">Kilos Reduced Per Month</span>
                </div>
                <input type="number" class="form-control" v-model="formData.kilosreducedpermonth" name="kilosreducedpermonth" placeholder="Kilos Reduced Per Month"/>
              </div>
              <div class="input-group mb-3">
                <div class="input-group-prepend">
                  <span class="input-group-text" id="input-goalsetting-date">Date</span>
                </div>
                <input type="datetime-local" class="form-control" v-model="formData.date" name="date" placeholder="Date"/>
              </div>
            </form>
            <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link" @click="addGoalSetting()">Add Goals</button>
          </div>
        </div>
        <div class="list-group list-group-flush">
          <div class="list-group-item d-flex align-items-start"
               v-for="(goalsetting,index) in goalSettingDetails" v-bind:key="index">
            <div class="mr-auto p-2">
              <span><a :href="`/goalsettingdetails/${goalsetting.id}`"> {{ goalsetting.kilosReducedPerMonth }} kilos reduced for the month {{ goalsetting.month }} </a></span>
            </div>
            <div class="p2">
              <a :href="`/goalsettingdetails/${goalsetting.id}`">
                <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link">
                  <i class="fa fa-pencil" aria-hidden="true"></i>
                </button>
              </a>
              <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link"
                      @click="deleteGoalSetting(goalsetting, index)">
                <i class="fas fa-trash" aria-hidden="true"></i>
              </button>
            </div>
          </div>
        </div>
      </div>

      <div class="card-footer text-left">
        <p  v-if="heartRateMonitorDetails.length == 0"> No HeartRate Monitoring Details yet...</p>
        <p  v-if="heartRateMonitorDetails.length > 0"> Heart Rate Monitoring Details so far...</p>
        <div class="card bg-light mb-3">
          <div class="card-header">
            <div class="row">
              <div class="col-6">
                Add HeartRate Monitoring details
              </div>
              <div class="col" align="right">
                <button rel="tooltip" title="Add"
                        class="btn btn-info btn-simple btn-link"
                        @click="hideForm =!hideForm">
                  <i class="fa fa-plus" aria-hidden="true"></i>
                </button>
              </div>
            </div>
          </div>
          <div class="card-body" :class="{ 'd-none': hideForm}">
            <form id="addHeartRateMonitor">
              <div class="input-group mb-3">
                <div class="input-group-prepend">
                  <span class="input-group-text" id="input-heartratemonitor-pulse">Pulse</span>
                </div>
                <input type="number" class="form-control" v-model="formData.pulse" name="pulse" placeholder="Pulse"/>
              </div>
              <div class="input-group mb-3">
                <div class="input-group-prepend">
                  <span class="input-group-text" id="input-heartratemonitor-date">Date</span>
                </div>
                <input type="datetime-local" class="form-control" v-model="formData.date" name="date" placeholder="Date"/>
              </div>
            </form>
            <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link" @click="addHeartRateMonitor()">Add HeartRate Montoring Details</button>
          </div>
        </div>
        <div class="list-group list-group-flush">
          <div class="list-group-item d-flex align-items-start"
               v-for="(heartratemonitor,index) in heartRateMonitorDetails" v-bind:key="index">
            <div class="mr-auto p-2">
              <span><a :href="`/heartratemonitordetails/${heartratemonitor.id}`"> Pulse was {{ heartratemonitor.pulse }} on {{ heartratemonitor.date }} </a></span>
            </div>
            <div class="p2">
              <a :href="`/heartratemonitordetails/${heartratemonitor.id}`">
                <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link">
                  <i class="fa fa-pencil" aria-hidden="true"></i>
                </button>
              </a>
              <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link"
                      @click="deleteHeartRateMonitor(heartratemonitor, index)">
                <i class="fas fa-trash" aria-hidden="true"></i>
              </button>
            </div>
          </div>
        </div>
      </div>

      <div class="card-footer text-left">
        <p  v-if="onlineConsultationDetails.length == 0"> No Consultation Details yet...</p>
        <p  v-if="onlineConsultationDetails.length > 0"> Consultation Details...</p>
        <div class="card bg-light mb-3">
          <div class="card-header">
            <div class="row">
              <div class="col-6">
                Add Consultation details
              </div>
              <div class="col" align="right">
                <button rel="tooltip" title="Add"
                        class="btn btn-info btn-simple btn-link"
                        @click="hideForm =!hideForm">
                  <i class="fa fa-plus" aria-hidden="true"></i>
                </button>
              </div>
            </div>
          </div>
          <div class="card-body" :class="{ 'd-none': hideForm}">
            <form id="addOnlineConsultation">
              <div class="input-group mb-3">
                <div class="input-group-prepend">
                  <span class="input-group-text" id="input-onlineconsultation-doctorname">Doctor's Name</span>
                </div>
                <input type="text" class="form-control" v-model="formData.doctorname" name="doctorname" placeholder="Doctor's Name"/>
              </div>
              <div class="input-group mb-3">
                <div class="input-group-prepend">
                  <span class="input-group-text" id="input-onlineconsultation-remarks">Remarks</span>
                </div>
                <input type="text" class="form-control" v-model="formData.remarks" name="remarks" placeholder="Remarks"/>
              </div>
              <div class="input-group mb-3">
                <div class="input-group-prepend">
                  <span class="input-group-text" id="input-onlineconsultation-appointmentdate">Appointment Date</span>
                </div>
                <input type="datetime-local" class="form-control" v-model="formData.appointmentdate" name="appointmentdate" placeholder="Appointment Date"/>
              </div>
            </form>
            <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link" @click="addOnlineConsultation()">Add Consultation Details</button>
          </div>
        </div>
        <div class="list-group list-group-flush">
          <div class="list-group-item d-flex align-items-start"
               v-for="(onlineconsultation,index) in onlineConsultationDetails" v-bind:key="index">
            <div class="mr-auto p-2">
              <span><a :href="`/onlineconsultationdetails/${onlineconsultation.id}`"> Remarks by {{ onlineconsultation.doctorName }}  -  {{ onlineconsultation.remarks }} </a></span>
            </div>
            <div class="p2">
              <a :href="`/onlineconsultationdetails/${onlineconsultation.id}`">
                <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link">
                  <i class="fa fa-pencil" aria-hidden="true"></i>
                </button>
              </a>
              <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link"
                      @click="deleteOnlineConsultation(onlineconsultation, index)">
                <i class="fas fa-trash" aria-hidden="true"></i>
              </button>
            </div>
          </div>
        </div>
      </div>

      <div class="card-footer text-left">
        <p  v-if="sleepTrackerDetails.length == 0"> No Sleep Tracking Details yet...</p>
        <p  v-if="sleepTrackerDetails.length > 0"> Sleep Tracking Details...</p>
        <div class="card bg-light mb-3">
          <div class="card-header">
            <div class="row">
              <div class="col-6">
                Add Sleep Tracking details
              </div>
              <div class="col" align="right">
                <button rel="tooltip" title="Add"
                        class="btn btn-info btn-simple btn-link"
                        @click="hideForm =!hideForm">
                  <i class="fa fa-plus" aria-hidden="true"></i>
                </button>
              </div>
            </div>
          </div>
          <div class="card-body" :class="{ 'd-none': hideForm}">
            <form id="addSleepTracker">
              <div class="input-group mb-3">
                <div class="input-group-prepend">
                  <span class="input-group-text" id="input-sleeptracker-hours">Hours</span>
                </div>
                <input type="number" class="form-control" v-model="formData.hours" name="hours" placeholder="Hours"/>
              </div>
              <div class="input-group mb-3">
                <div class="input-group-prepend">
                  <span class="input-group-text" id="input-sleeptracker-date">Date</span>
                </div>
                <input type="datetime-local" class="form-control" v-model="formData.date" name="date" placeholder="Date"/>
              </div>
            </form>
            <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link" @click="addSleepTracker()">Add Sleep Tracking Details</button>
          </div>
        </div>
        <div class="list-group list-group-flush">
          <div class="list-group-item d-flex align-items-start"
               v-for="(sleeptracker,index) in sleepTrackerDetails" v-bind:key="index">
            <div class="mr-auto p-2">
              <span><a :href="`/sleeptrackerdetails/${sleeptracker.id}`"> {{ sleeptracker.hours }} hours on {{ sleeptracker.date }} </a></span>
            </div>
            <div class="p2">
              <a :href="`/sleeptrackerdetails/${sleeptracker.id}`">
                <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link">
                  <i class="fa fa-pencil" aria-hidden="true"></i>
                </button>
              </a>
              <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link"
                      @click="deleteSleepTracker(foodtracker, index)">
                <i class="fas fa-trash" aria-hidden="true"></i>
              </button>
            </div>
          </div>
        </div>
      </div>

    </div>
  </app-layout>
</template>

<script>
Vue.component("user-profile", {
  template: "#user-profile",
  data: () => ({
    user: null,
    noUserFound: false,
    activities: [],
    foodTrackerDetails: [],
    goalSettingDetails: [],
    heartRateMonitorDetails: [],
    onlineConsultationDetails: [],
    sleepTrackerDetails: [],
    formData: [],
    hideForm :true,
  }),
  created: function () {
    const userId = this.$javalin.pathParams["user-id"];
    const url = `/api/users/${userId}`
    axios.get(url)
        .then(res => this.user = res.data)
        .catch(error => {
          console.log("No user found for id passed in the path parameter: " + error)
          this.noUserFound = true
        })
    axios.get(url + `/activities`)
        .then(res => this.activities = res.data)
        .catch(error => {
          console.log("No activities added yet (this is ok): " + error)
        })
    axios.get(url + `/foodtrackerdetails`)
        .then(res => this.foodTrackerDetails = res.data)
        .catch(error => {
          console.log("No details added yet (this is ok): " + error)
        })
    axios.get(url + `/goalsettingdetails`)
        .then(res => this.goalSettingDetails = res.data)
        .catch(error => {
          console.log("No details added yet (this is ok): " + error)
        })
    axios.get(url + `/heartratemonitordetails`)
        .then(res => this.heartRateMonitorDetails = res.data)
        .catch(error => {
          console.log("No details added yet (this is ok): " + error)
        })
    axios.get(url + `/onlineconsultationdetails`)
        .then(res => this.onlineConsultationDetails = res.data)
        .catch(error => {
          console.log("No details added yet (this is ok): " + error)
        })
    axios.get(url + `/sleeptrackerdetails`)
        .then(res => this.sleepTrackerDetails = res.data)
        .catch(error => {
          console.log("No details added yet (this is ok): " + error)
        })
  },
  methods: {
    updateUser: function () {
      const userId = this.$javalin.pathParams["user-id"];
      const url = `/api/users/${userId}`
      axios.patch(url,
          {
            name: this.user.name,
            email: this.user.email
          })
          .then(response =>
              this.user.push(response.data))
          .catch(error => {
            console.log(error)
          })
      alert("User updated!")
    },
    deleteUser: function () {
      if (confirm("Do you really want to delete?")) {
        const userId = this.$javalin.pathParams["user-id"];
        const url = `/api/users/${userId}`
        axios.delete(url)
            .then(response => {
              alert("User deleted")
              //display the /users endpoint
              window.location.href = '/users';
            })
            .catch(function (error) {
              console.log(error)
            });
      }
    },
    deleteActivity: function (Activity, index) {
      if (confirm('Are you sure you want to delete this activity? This action cannot be undone.', 'Warning')) {
        const activityId = Activity.id;
        const url = `/api/activities/${activityId}`;
        axios.delete(url)
            .then(response =>
                //delete from the local state so Vue will reload list automatically
                this.activities.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error)
            });
      }
    },
    addActivity: function (){
      const userId = this.$javalin.pathParams["user-id"];
      const url = `/api/activities`;
      axios.post(url,
          {
            description: this.formData.description,
            duration: this.formData.duration,
            calories: this.formData.calories,
            started: this.formData.date,
            userId: userId
          })
          .then(response => {
            this.activities.push(response.data)
            this.hideForm= true;
          })
          .catch(error => {
            console.log(error)
          })
    },
    deleteFoodTracker: function (FoodTracker, index) {
      if (confirm('Are you sure you want to delete this? This action cannot be undone.', 'Warning')) {
        const foodtrackerId = FoodTracker.id;
        const url = `/api/foodtrackerdetails/${foodtrackerId}`;
        axios.delete(url)
            .then(response =>
                //delete from the local state so Vue will reload list automatically
                this.foodTrackerDetails.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error)
            });
      }
    },
    addFoodTracker: function (){
      const userId = this.$javalin.pathParams["user-id"];
      const url = `/api/foodtrackerdetails`;
      axios.post(url,
          {
            meal: this.formData.meal,
            caloriesIntake: this.formData.caloriesintake,
            date: this.formData.date,
            userId: userId
          })
          .then(response => {
            this.foodTrackerDetails.push(response.data)
            this.hideForm= true;
          })
          .catch(error => {
            console.log(error)
          })
    },
    deleteGoalSetting: function (GoalSetting, index) {
      if (confirm('Are you sure you want to delete this? This action cannot be undone.', 'Warning')) {
        const goalsettingId = GoalSetting.id;
        const url = `/api/goalsettingdetails/${goalsettingId}`;
        axios.delete(url)
            .then(response =>
                //delete from the local state so Vue will reload list automatically
                this.goalSettingDetails.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error)
            });
      }
    },
    addGoalSetting: function (){
      const userId = this.$javalin.pathParams["user-id"];
      const url = `/api/goalsettingdetails`;
      axios.post(url,
          {
            month: this.formData.month,
            bodyFatPercentage: this.formData.bodyfatpercentage,
            kilosReducedPerMonth: this.formData.kilosreducedpermonth,
            date: this.formData.date,
            userId: userId
          })
          .then(response => {
            this.goalSettingDetails.push(response.data)
            this.hideForm= true;
          })
          .catch(error => {
            console.log(error)
          })
    },

    deleteHeartRateMonitor: function (HeartRateMonitor, index) {
      if (confirm('Are you sure you want to delete this? This action cannot be undone.', 'Warning')) {
        const heartratemonitorId = HeartRateMonitor.id;
        const url = `/api/heartratemonitordetails/${heartratemonitorId}`;
        axios.delete(url)
            .then(response =>
                //delete from the local state so Vue will reload list automatically
                this.heartRateMonitorDetails.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error)
            });
      }
    },
    addHeartRateMonitor: function (){
      const userId = this.$javalin.pathParams["user-id"];
      const url = `/api/heartratemonitordetails`;
      axios.post(url,
          {
            pulse: this.formData.pulse,
            date: this.formData.date,
            userId: userId
          })
          .then(response => {
            this.heartRateMonitorDetails.push(response.data)
            this.hideForm= true;
          })
          .catch(error => {
            console.log(error)
          })
    },

    deleteOnlineConsultation: function (OnlineConsultation, index) {
      if (confirm('Are you sure you want to delete this? This action cannot be undone.', 'Warning')) {
        const onlineConsultationId = OnlineConsultation.id;
        const url = `/api/onlineconsultationdetails/${onlineConsultationId}`;
        axios.delete(url)
            .then(response =>
                //delete from the local state so Vue will reload list automatically
                this.onlineConsultationDetails.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error)
            });
      }
    },
    addOnlineConsultation: function (){
      const userId = this.$javalin.pathParams["user-id"];
      const url = `/api/onlineconsultationdetails`;
      axios.post(url,
          {
            doctorName: this.formData.doctorname,
            remarks: this.formData.remarks,
            appointmentDate: this.formData.appointmentdate,
            userId: userId
          })
          .then(response => {
            this.onlineConsultationDetails.push(response.data)
            this.hideForm= true;
          })
          .catch(error => {
            console.log(error)
          })
    },

    deleteSleepTracker: function (SleepTracker, index) {
      if (confirm('Are you sure you want to delete this? This action cannot be undone.', 'Warning')) {
        const sleeptrackerId = SleepTracker.id;
        const url = `/api/sleeptrackerdetails/${sleeptrackerId}`;
        axios.delete(url)
            .then(response =>
                //delete from the local state so Vue will reload list automatically
                this.sleepTrackerDetails.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error)
            });
      }
    },
    addSleepTracker: function (){
      const userId = this.$javalin.pathParams["user-id"];
      const url = `/api/sleeptrackerdetails`;
      axios.post(url,
          {
            hours: this.formData.hours,
            date: this.formData.date,
            userId: userId
          })
          .then(response => {
            this.sleepTrackerDetails.push(response.data)
            this.hideForm= true;
          })
          .catch(error => {
            console.log(error)
          })
    }
  }
});
</script>

