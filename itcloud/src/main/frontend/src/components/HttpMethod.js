import axios from "axios";

const HttpMethod = ({ method, url, data }) => {
  return axios({
    method: method,
    url: url,
    data: data,
    headers: {
      'Content-Type': 'application/json',
    },
  })
  .then((response) => {
    alert("success");
  })
  .catch((error) => {
    console.error("API 요청 실패:", error.response ? error.response.data : error);
    alert("fail");
  });
};
  export default HttpMethod;