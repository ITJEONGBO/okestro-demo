$(function() {
    $('#aside > div:nth-child(2)').click(function(){
        location.href = 'http://127.0.0.1:5500/Darkdashboard2.html';
    });
    $('#small_menu > div:nth-child(4)').click(function(){
        location.href = 'http://127.0.0.1:5500/Darkdashboard2.html';
    });
   
    // aside 팝업
    $('#aside_popup_btn').click(function(){
        if ($(window).width() > 1000) {
            if($('#aside_popup').css('display') === 'none'){
                $('#aside_popup').css('display', 'block');
                $('#main_content').css({'width':'88.05%','margin-left':'240px'});
            } else {
                $('#aside_popup').css('display', 'none');
                $('#main_content').css({'width':'98.05%','margin-left':'97px'})
            }
        } else {
            $('#aside_popup').css('display', 'none');
            $('#main_content').css({'width':'98.05%','margin-left':'97px'})
        }
    });  
    // 화면 너비가 600px 이하일 때는 팝업을 숨김
    $(window).resize(function() {
        if ($(window).width() <= 1000) {
            $('#aside_popup').css('display', 'none');
            $('#main_content').css({'width':'98.05%','margin-left':'97px'});
        }
    });
    $(window).resize(function() {
        if ($(window).width() <= 600) {
            $('#main_content').css({'margin-left':'0','width':'100%'});
            $('#header_right>div:lt(3)').css('display', 'none'); // 앞의 3개 div 숨기기
            $('#header_right>div:last-child').css('display', 'block'); // 마지막 div 보이기
            
        }else{
            $('#main_content').css({'margin-left':'97px','width':'98.05%'});
            $('#header_right>div:lt(3)').css('display', 'block');
            $('#header_right>div:last-child').css('display', 'none');
        } 
    });

    //모바일화면일때 상단메뉴클릭
    $('#header_right>div:last-child>i').click(function(){
        if($('#small_menu').css('display') === 'none'){
            $('#small_menu').css('display', 'block');
            $('#small_menu').css('display', 'grid');

        } else {
            $('#small_menu').css('display', 'none');
        }
    });  

    
});