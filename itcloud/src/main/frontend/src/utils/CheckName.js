export function CheckKorenName(name) {

  const hasKorean = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/.test(name);
  if (hasKorean) {
    return false; // 한글이 포함된 경우 false 반환
  } 
  return true; // 검사를 통과한 경우 true 반환
}

export function CheckName(name) {
  if(!name){
    return false;  // 이름이 입력되지 않은경우
  }
  return true; // 검사를 통과한 경우 true 반환
}