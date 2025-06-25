<template>
	<h3>登入</h3>
        <table>
        <tbody>
			<tr>
			<td>ID : </td>
			<td><input type="text" name="username" v-model="username" @blur="checkUsername()" @focus="message=''"></td>
			<td><span>{{message}}</span></td>
			</tr>
		<tr>
			<td>PWD : </td>
			<td><input type="text" name="password" v-model="password"></td>
			<td></td>
		</tr>
		<tr>
			<td> </td>
			<td align="right"><input type="submit" value="login" @click="login()"></td>
		</tr>
    </tbody>
	</table>
</template>
    
<script setup>
    import Swal from 'sweetalert2';
	import axios from '@/plugins/axios.js';
	import { ref } from 'vue';
	import { useRouter } from 'vue-router';
	const router = useRouter();
	import useUserStore from '@/stores/user.js';
	const userStore = useUserStore();

	const username = ref("");
	const password = ref("");

		const login = ()=> {

			if (username.value == "") {
				username.value = null;
			}

			if (password.value == "") {
				password.value = null;
			}

			let request = {
				username: username.value,
				password: password.value
			};

			// sessionStorage.removeItem("user");
			// sessionStorage.removeItem("token");

			userStore.setEmail("");
			userStore.setToken("");


			axios.post("/ajax/secure/login", request).then(response => {
				if (response.data.success) {
					console.log("response", response);
					Swal.fire({
						icon: 'success',
						title: '登入成功',
						text: '歡迎 ' + request.username
					}).then(result => {
						if (result.isConfirmed) {
							// sessionStorage.setItem("user", response.data.user);
							// sessionStorage.setItem("token", response.data.token);
							userStore.setEmail(response.data.user);
							userStore.setToken(response.data.token);
							// window.location.href = "/";
							router.push("/");
						}
					})
				} else {
					Swal.fire({
						icon: 'warning',
						title: '登入失敗',
						text: response.data.message
					})
				}
			}).catch(error => {
				console.log("error", error);
				Swal.fire({
					icon: 'error',
					title: '登入出現錯誤',
					text: error.message
				})
			})

		}

		const message = ref("");

async function checkUsername() {
	if(username.value==="") {
		username.value = null;
	}
	const body = {
		"username": username.value,
	};
	try {
		const response = await axios.post("/ajax/secure/login/exists", body);
		message.value = response.data;
		console.log("response", response.data);
	} catch (error) {
		console.log("error", error);
	}
}





</script>
    
<style>
    
</style>