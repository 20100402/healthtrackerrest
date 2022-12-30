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
        <p  v-if="activities.length == 0"> No FoodTracker Details yet...</p>
        <p  v-if="activities.length > 0"> FoodTracker Details so far...</p>
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
              <span><a :href="`/foodtrackerdetails/${foodtracker.id}`"> {{ foodtracker.caloriesintake }} for {{ foodtracker.meal }} </a></span>
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
    }
  }
});
</script>

