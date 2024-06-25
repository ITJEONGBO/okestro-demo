$(function(){
   

// 처음 로딩 시 첫 번째 섹션을 기본으로 활성화
setTimeout(function() {
    $('#aside_popup_dashboard_btn').first().trigger('click');
}, 0);

var previousButton = null;

$('#aside_popup_dashboard_btn').click(function(){
    var currentButton = $(this);
    
    // 이전에 클릭한 버튼이 현재 버튼과 다르면 페이지 새로고침
    if (previousButton && previousButton.attr('id') !== currentButton.attr('id')) {
        location.reload();
    }

    $('#dash_board').css('display','block');
    $('#section').css('display','none');
    $('#storage_section').css('display','none');
    $('#network_section').css('display','none');
    $('#setting_section').css('display','none');

    $('#virtual_machine_chart').css('display','none');
    $('#storage_chart').css('display','none');
    $('#network_chart').css('display','none');
    $('#setting_chart').css('display','none');
    $('#setting_user_section').css('display','none');

    if ($('#aside_popup').css('display') === 'none' || $('#aside_popup_dashboard_btn').css('background-color') !== 'rgb(218, 236, 245)') {
        $('#aside_popup_dashboard_btn').css('background-color', 'rgb(218, 236, 245)'); // 배경색을 노란색으로 변경
        $('#aside_popup_machine_btn').css('background-color', ''); // 다른 버튼 초기화
        $('#aside_popup_storage_btn').css('background-color', ''); // 다른 버튼 초기화
        $('#aside_popup_network_btn').css('background-color', ''); // 다른 버튼 초기화
        $('#setting_icon').css('background-color', ''); // 다른 버튼 초기화
    } else {
        $('#aside_popup').css('display', 'none');
        $('#aside_outer').css('width','3%');
    }

    // 현재 버튼을 previousButton에 저장
    previousButton = currentButton;
});

$('#aside_popup_machine_btn, #aside_popup_storage_btn, #aside_popup_network_btn').click(function() {
    // 클릭된 버튼을 previousButton에 저장
    previousButton = $(this);
});

    
   
    // aside 팝업(가상머신버튼)
    $('#aside_popup_machine_btn').click(function(){
            $('#dash_board').css('display','none');
            $('#section').css('display','block');
            $('#storage_section').css('display','none');
            $('#network_section').css('display','none');
            $('#setting_section').css('display','none');
            $('.content_detail_section').css('display','none');

            $('#virtual_machine_chart').css('display','block');
            $('#storage_chart').css('display','none');
            $('#network_chart').css('display','none');
            $('#setting_chart').css('display','none');
            $('#setting_user_section').css('display','none');
        if ($('#aside_popup').css('display') === 'none' || $('#aside_popup_machine_btn').css('background-color') !== 'rgb(218, 236, 245)') { //노란색
            $('#aside_popup').css('display', 'block');
            $('#aside_outer').css('width','20%');
            $('#aside_popup_machine_btn').css('background-color', 'rgb(218, 236, 245)'); // 배경색을 노란색으로 변경
            $('#aside_popup_dashboard_btn').css('background-color', '');
            $('#aside_popup_storage_btn').css('background-color', ''); // 다른 버튼 초기화
            $('#aside_popup_network_btn').css('background-color', ''); // 다른 버튼 초기화
            $('#setting_icon').css('background-color', ''); // 다른 버튼 초기화
        } else {
            $('#aside_popup').css('display', 'none');
            $('#aside_outer').css('width','3%');
           
        }
    });
    // aside 팝업(스토리지 버튼)
    $('#aside_popup_storage_btn').click(function(){    
        $('#dash_board').css('display','none');  
            $('#section').css('display','none');
            $('#storage_section').css('display','block');
            $('#network_section').css('display','none');
            $('#setting_section').css('display','none');
            $('.content_detail_section').css('display','none');

            $('#virtual_machine_chart').css('display','none');
            $('#storage_chart').css('display','block');
            $('#network_chart').css('display','none');
            $('#setting_chart').css('display','none');
            $('#setting_user_section').css('display','none');
        if ($('#aside_popup').css('display') === 'none' || $('#aside_popup_storage_btn').css('background-color') !== 'rgb(218, 236, 245)') {
            $('#aside_popup').css('display', 'block');
            $('#aside_outer').css('width','20%');
            $('#aside_popup_storage_btn').css('background-color', 'rgb(218, 236, 245)'); // 배경색을 노란색으로 변경
            $('#aside_popup_dashboard_btn').css('background-color', '');
            $('#aside_popup_machine_btn').css('background-color', ''); // 다른 버튼 초기화
            $('#aside_popup_network_btn').css('background-color', ''); // 다른 버튼 초기화
            $('#setting_icon').css('background-color', ''); // 다른 버튼 초기화
            
         } else {
             $('#aside_popup').css('display', 'none');
             $('#aside_outer').css('width','3%');
             
         }
    });
    // aside 팝업(네트워크 버튼)
    $('#aside_popup_network_btn').click(function(){
        $('#dash_board').css('display','none');
        $('#section').css('display','none');
        $('#storage_section').css('display','none');
        $('#network_section').css('display','block');
        $('#setting_section').css('display','none');
        $('.content_detail_section').css('display','none');

        $('#virtual_machine_chart').css('display','none');
        $('#storage_chart').css('display','none');
        $('#network_chart').css('display','block');
        $('#setting_chart').css('display','none');
        $('#setting_user_section').css('display','none');
        if ($('#aside_popup').css('display') === 'none' || $('#aside_popup_network_btn').css('background-color') !== 'rgb(218, 236, 245)') {
            $('#aside_popup').css('display', 'block');
            $('#aside_outer').css('width','20%');
            $('#aside_popup_network_btn').css('background-color', 'rgb(218, 236, 245)'); // 배경색을 노란색으로 변경
            $('#aside_popup_dashboard_btn').css('background-color', '');
            $('#aside_popup_machine_btn').css('background-color', ''); // 다른 버튼 초기화
            $('#aside_popup_storage_btn').css('background-color', ''); // 다른 버튼 초기화
            $('#setting_icon').css('background-color', ''); // 다른 버튼 초기화
          
         } else {
             $('#aside_popup').css('display', 'none');
             $('#aside_outer').css('width','3%');
             
         }
    });
    // aside 팝업(세팅 버튼 수정필요)
    $('#setting_icon').click(function(){
        $('#dash_board').css('display','none');
        $('#section').css('display','none');
        $('#storage_section').css('display','none');
        $('#network_section').css('display','none');
        $('#setting_section').css('display','block');
        $('.content_detail_section').css('display','none');

        $('#virtual_machine_chart').css('display','none');
        $('#storage_chart').css('display','none');
        $('#network_chart').css('display','none');
        $('#setting_chart').css('display','block');
        if ($('#aside_popup').css('display') === 'none' || $('#setting_icon').css('background-color') !== 'rgb(218, 236, 245)') {
            $('#aside_popup').css('display', 'block');
            $('#aside_outer').css('width','20%');
            $('#setting_icon').css('background-color', 'rgb(218, 236, 245)'); // 배경색을 노란색으로 변경
            $('#aside_popup_dashboard_btn').css('background-color', '');
            $('#aside_popup_machine_btn').css('background-color', ''); // 다른 버튼 초기화
            $('#aside_popup_network_btn').css('background-color', ''); // 다른 버튼 초기화
            $('#aside_popup_storage_btn').css('background-color', ''); // 다른 버튼 초기화
            

         } else {
             $('#aside_popup').css('display', 'none');
             $('#aside_outer').css('width','3%');

         }
    });

    // content_detail_section
    $('.section_table_outer td:first-child').click(function(){
        $('#dash_board').css('display','none');
        $('#section').css('display','none');
        $('#storage_section').css('display','none');
        $('#network_section').css('display','none');
        $('#setting_section').css('display','none');
        $('.content_detail_section').css('display','block');

        $('#virtual_machine_chart').css('display','none');
        $('#storage_chart').css('display','none');
        $('#network_chart').css('display','none');
        $('#setting_chart').css('display','none');
        if ($('#aside_popup').css('display') === 'none' || $('#setting_icon').css('background-color') !== 'rgb(218, 236, 245)') {
            $('#aside_popup').css('display', 'block');
            $('#aside_outer').css('width','20%');
            $('#setting_icon').css('background-color', 'rgb(218, 236, 245)'); // 배경색을 노란색으로 변경
            $('#aside_popup_dashboard_btn').css('background-color', '');
            $('#aside_popup_machine_btn').css('background-color', ''); // 다른 버튼 초기화
            $('#aside_popup_network_btn').css('background-color', ''); // 다른 버튼 초기화
            $('#aside_popup_storage_btn').css('background-color', ''); // 다른 버튼 초기화
            

         } else {
             $('#aside_popup').css('display', 'none');
             $('#aside_outer').css('width','3%');
         }
    });

    // aside차트(가상머신)
    $('#aside_popup_first').click(function(){
        $('#aside_popup_second').toggle();
        $('#aside_popup_last').css('display','none');
    });
    $('#aside_popup_second').click(function(){
        $('#aside_popup_last').toggle();
    });
    // aside차트(스토리지)
    $('#aside_popup_first2').click(function(){
        $('#aside_popup_second2').toggle();
        $('#aside_popup_last2').css('display','none');
    });
    $('#aside_popup_second2').click(function(){
        $('#aside_popup_last2').toggle();
    });
    // aside차트(네트워크)
    $('#aside_popup_first3').click(function(){
        $('#aside_popup_second3').toggle();
    });
    


    // aside메뉴들 색
    $("#nav>div").click(function(){
        // 클릭한 div의 현재 글자색가져오기
        var currentColor = $(this).css("color");
        // 만약 현재 글자색이 검정이라면
        if (currentColor === "black") {
            // 계속 검정색유지
            $(this).css("color", "black");
        } else { // 만약 검정이아니라면
            $("#nav>div").css('color','rgb(143, 143, 143)');
            $(this).css("color", "black");
        }
    });

    $('#popup_btn').click(function(){
        if($('#popup_box').css('display') === 'none'){
            $('#popup_box').css('display', 'block');
        } else {
            $('#popup_box').css('display', 'none');
        }
    });

    var previousDiv = null;

    // 우클릭 메뉴박스
    $('#aside_popup_last div').contextmenu(function(event){
        // 만약 이전에 클릭한 div가 있으면 배경색 초기화
        if (previousDiv !== null) {
            previousDiv.css('background-color', '');
        }
    
        var currentColor = $(this).css("background-color");
    
        // 현재 div의 배경색을 변경하고, previousDiv에 현재 div 저장
        if (currentColor === "none" || currentColor === "rgba(0, 0, 0, 0)") {
            $(this).css("background-color", "none");
        } else {
            $(this).css('background-color', '#e6eefa');
        }
    
        previousDiv = $(this);
        
        event.preventDefault();
        $('#context_menu').css({
            display: 'block',
            left: event.pageX,
            top: event.pageY
        });
    });
    
    // 문서의 다른 곳을 클릭하면 배경색 초기화
    $(document).click(function(){
        $('#context_menu').hide();
        if (previousDiv !== null) {
            previousDiv.css('background-color', '');
            previousDiv = null; // 초기화 후 previousDiv 리셋
        }
    });
    
    $('#context_menu').click(function(event){
        event.stopPropagation();
    });
    

    // 스토리지 우클릭 메뉴박스
    $('.section_table_outer table tbody tr').contextmenu(function(event){
        var currentColor = $(this).css("background-color");
        // 만약 현재 글자색이 검정이라면
        if (currentColor === "none") {
            // 계속 검정색유지
            $(this).css("background-color", "none");
        } else { // 만약 검정이아니라면
            $(this).css('background-color','#e6eefa');
        }
        
        event.preventDefault();
        $('#storage_context_menu').css({
            display: 'block',
            left: event.pageX,
            top: event.pageY
        });
    });

    $(document).click(function(){
        $('#storage_context_menu').hide();
    });

    $('#storage_context_menu').click(function(event){
        event.stopPropagation();
    });

       // 가져오기 팝업창
       $('.get_btn').click(function(){
        if($('#get_popup_bg').css('display') === 'none'){
            $('#get_popup_bg').css('display', 'block');
        } else {
            $('#get_popup_bg').css('display', 'none');
        }
    });
    $('.domain_header>button').click(function(){
        $('#get_popup_bg').css('display', 'none');
    });
    $('#get_footer_btn').click(function(){
        $('#get_popup_bg').css('display', 'none');
    });
    //템플릿 생성 팝업창
    $('#template_btn').click(function(){
        if($('#template_bg').css('display') === 'none'){
            $('#template_bg').css('display', 'block');
        } else {
            $('#template_bg').css('display', 'none');
        }
    });
    $('.domain_header>button').click(function(){
        $('#template_bg').css('display', 'none');
    });
    $('#template_footer_btn').click(function(){
        $('#template_bg').css('display', 'none');
    });
   
    // 도메인팝업창1
    $('#domain').click(function(){
        if($('#popup_bg').css('display') === 'none'){
            $('#popup_bg').css('display', 'block');
        } else {
            $('#popup_bg').css('display', 'none');
        }
    });
    $('.domain_header>button').click(function(){
        $('#popup_bg').css('display', 'none');
    });
    $('#domain_footer>a:last-child').click(function(){
        $('#popup_bg').css('display', 'none');
    });

    // 도메인팝업창2
    $('#domain2').click(function(){
        if($('#popup_bg2').css('display') === 'none'){
            $('#popup_bg2').css('display', 'block');
        } else {
            $('#popup_bg2').css('display', 'none');
        }
    });
    $('.domain_header>button').click(function(){
        $('#popup_bg2').css('display', 'none');
    });
    $('#domain2_footer>button').click(function(){
        $('#popup_bg2').css('display', 'none');
    });
   

    //  마이그레이션 팝업창
    $('#migration_btn').click(function(){
        if($('#migration_popup_outer').css('display') === 'none'){
            $('#migration_popup_outer').css('display', 'block');
        } else {
            $('#migration_popup_outer').css('display', 'none');
        }
    });
    $('.domain_header>button').click(function(){
        $('#migration_popup_outer').css('display', 'none');
    });
    $('#migration_footer>a:last-child').click(function(){
        $('#migration_popup_outer').css('display', 'none');
    });

    //  ova 팝업창
    $('#ova_btn').click(function(){
        if($('#ova_outer').css('display') === 'none'){
            $('#ova_outer').css('display', 'block');
        } else {
            $('#ova_outer').css('display', 'none');
        }
    });
    $('.domain_header>button').click(function(){
        $('#ova_outer').css('display', 'none');
    });
    $('#ova_footer>button:last-child').click(function(){
        $('#ova_outer').css('display', 'none');
    });


    //편집팝업창
    $('#edit_btn').click(function(){ 
        if($('#edit_popup_bg').css('display') === 'none'){
            $('#edit_popup_bg').css('display', 'block');
        } else {
            $('#edit_popup_bg').css('display', 'none');
        }
        // 일반버튼으로 시작
        $('#common_btn').trigger('click');
    });
    $('.edit_header>button').click(function(){
        $('#edit_popup_bg').css('display', 'none');
    });
    $('.edit_footer>button:nth-child(3)').click(function(){
        $('#edit_popup_bg').css('display', 'none');
    });
    //편집팝업 aside
    $('.edit_aside > div').click(function() {
        $('.edit_aside > div i').css('display', 'none');
        $('.edit_aside > div').css({'background-color':'#FAFAFA','color': 'black','border-bottom':'none'});

        $(this).find('i').css('display', 'block');
        $(this).css({'background-color':'#EDEDED','color': '#1eb8ff','border-bottom':'1px solid blue'});
    });
    
    // nav 파란밑줄, 각 맞는 창불러오기 
    $('.content_header_left div').click(function() {
        var index = $(this).index();
        var parentId = $(this).closest('.content_header').parent().attr('id');

        $('.content_header_left div').removeClass('active');
        $(this).addClass('active');

        if (index === 0) {
            $('.content_outer').show();
            $('#network_outer').hide();
            $('#disk_outer').hide();
            $('#snapshot_outer').hide();
            $('#application_outer').hide();
            $('#pregroup_outer').hide();
            $('#pregroup_lable_outer').hide();
            $('#guest_info _outer').hide();
            $('#power_outer').hide();
            $('#event_outer').hide();
        } else if (index === 1) {
            $('.content_outer').hide();
            $('#network_outer').show();
            $('#disk_outer').hide();
            $('#snapshot_outer').hide();
            $('#application_outer').hide();
            $('#pregroup_outer').hide();
            $('#pregroup_lable_outer').hide();
            $('#guest_info_outer').hide();
            $('#power_outer').hide();
            $('#event_outer').hide();
        } else if (index === 2) {
            $('.content_outer').hide();
            $('#network_outer').hide();
            $('#disk_outer').show();
            $('#snapshot_outer').hide();
            $('#application_outer').hide();
            $('#pregroup_outer').hide();
            $('#pregroup_lable_outer').hide();
            $('#guest_info_outer').hide();
            $('#power_outer').hide();
            $('#event_outer').hide();
        }else if (index === 3) {
            $('.content_outer').hide();
            $('#network_outer').hide();
            $('#disk_outer').hide();
            $('#snapshot_outer').show();
            $('#application_outer').hide();
            $('#pregroup_outer').hide();
            $('#pregroup_lable_outer').hide();
            $('#guest_info_outer').hide();
            $('#power_outer').hide();
            $('#event_outer').hide();
        }else if (index === 4) {
            $('.content_outer').hide();
            $('#network_outer').hide();
            $('#disk_outer').hide();
            $('#snapshot_outer').hide();
            $('#application_outer').show();
            $('#pregroup_outer').hide();
            $('#pregroup_lable_outer').hide();
            $('#guest_info_outer').hide();
            $('#power_outer').hide();
            $('#event_outer').hide();
        }else if (index === 5) {
            $('.content_outer').hide();
            $('#network_outer').hide();
            $('#disk_outer').hide();
            $('#snapshot_outer').hide();
            $('#application_outer').hide();
            $('#pregroup_outer').show();
            $('#pregroup_lable_outer').hide();
            $('#guest_info_outer').hide();
            $('#power_outer').hide();
            $('#event_outer').hide();
        }else if (index === 6) {
            $('.content_outer').hide();
            $('#network_outer').hide();
            $('#disk_outer').hide();
            $('#snapshot_outer').hide();
            $('#application_outer').hide();
            $('#pregroup_outer').hide();
            $('#pregroup_lable_outer').show();
            $('#guest_info_outer').hide();
            $('#power_outer').hide();
            $('#event_outer').hide();
        }else if (index === 7) {
            $('.content_outer').hide();
            $('#network_outer').hide();
            $('#disk_outer').hide();
            $('#snapshot_outer').hide();
            $('#application_outer').hide();
            $('#pregroup_outer').hide();
            $('#pregroup_lable_outer').hide();
            $('#guest_info_outer').show();
            $('#power_outer').hide();
            $('#event_outer').hide();
        }else if (index === 8) {
            $('.content_outer').hide();
            $('#network_outer').hide();
            $('#disk_outer').hide();
            $('#snapshot_outer').hide();
            $('#application_outer').hide();
            $('#pregroup_outer').hide();
            $('#pregroup_lable_outer').hide();
            $('#guest_info_outer').hide();
            $('#power_outer').show();
            $('#event_outer').hide();
        }else if (index === 9) {
            $('.content_outer').hide();
            $('#network_outer').hide();
            $('#disk_outer').hide();
            $('#snapshot_outer').hide();
            $('#application_outer').hide();
            $('#pregroup_outer').hide();
            $('#pregroup_lable_outer').hide();
            $('#guest_info_outer').hide();
            $('#power_outer').hide();
            $('#event_outer').show();
        }

        
        $('.content_outer .content_header_left div').eq(index).addClass('active');
        $('#network_outer .content_header_left div').eq(index).addClass('active');
        $('#disk_outer .content_header_left div').eq(index).addClass('active');
        $('#snapshot_outer .content_header_left div').eq(index).addClass('active');
        $('#application_outer .content_header_left div').eq(index).addClass('active');
        $('#pregroup_outer .content_header_left div').eq(index).addClass('active');
        $('#pregroup_lable_outer .content_header_left div').eq(index).addClass('active');
        $('#guest_info_outer .content_header_left div').eq(index).addClass('active');
        $('#power_outer .content_header_left div').eq(index).addClass('active');
        $('#event_outer .content_header_left div').eq(index).addClass('active');
    });

    //네트워크 인터페이스(새로만들기) 팝업창
    $('#network_popup_new').click(function(){
        if($('.network_popup_outer').css('display') === 'none'){
            $('.network_popup_outer').css('display', 'block');
        } else {
            $('.network_popup_outer').css('display', 'none');
        }
    });
    $('.network_popup_header>button').click(function(){
        $('.network_popup_outer').css('display', 'none');
    });
    $('.edit_footer>button:last-child').click(function(){
        $('.network_popup_outer').css('display', 'none');
    });

    // 디스크(새로만들기) nav클릭
    $("#disk_new_nav>div").click(function(){
        // 클릭한 div의 현재 글자색가져오기
        var currentColor = $(this).css("color");
        // 만약 현재 글자색이 회색이라면
        if (currentColor === "rgb(35, 132, 243)") {
            // 계속 검정색유지
            $(this).css("color", "rgb(35, 132, 243)");
        } else { // 만약 회색이아니라면
            $("#disk_new_nav>div").css({
                "color": "black",
                "border-bottom": "none",
                "font-weight": "400"
            });
            $(this).css({
                "color": "rgb(35, 132, 243)",
                "border-bottom": "2px solid blue",
                "font-weight": "800"
            });
        }
    });

    //디스크(새로만들기) 팝업창
    $('#disk_popup_new').click(function(){
        if($('.disk_popup_outer').css('display') === 'none'){
            $('.disk_popup_outer').css('display', 'block');
        } else {
            $('.disk_popup_outer').css('display', 'none');
        }
    });
    $('.network_popup_header>button').click(function(){
        $('.disk_popup_outer').css('display', 'none');
    });
    $('.edit_footer>button:last-child').click(function(){
        $('.disk_popup_outer').css('display', 'none');
    });
    // 디스크(새로만들기 -> 이미지)
    $('#new_img_btn').click(function(){
        $('.disk_new_img').css('display', 'flex');
        $('#directlun_outer').css('display', 'none');
        $('#managed_block_outer').css('display', 'none');
    });
    // 디스크(새로만들기 -> 직접Lun)
    $('#directlun_btn').click(function(){
        $('#directlun_outer').css('display', 'block');
        $('.disk_new_img').css('display', 'none');
        $('#managed_block_outer').css('display', 'none');
    });
    
     // 디스크(새로만들기 -> 관리되는 블록
     $('#managed_block_btn').click(function(){
        $('#managed_block_outer').css('display', 'flex');
        $('#directlun_outer').css('display', 'none');
        $('.disk_new_img').css('display', 'none');
    });

    
   //디스크(연결) 팝업창
   $('#join_popup_btn').click(function(){
        if($('#join_popup_outer').css('display') === 'none'){
            $('#join_popup_outer').css('display', 'block');
        } else {
            $('#join_popup_outer').css('display', 'none');
        }
    });
    $('.network_popup_header>button').click(function(){
        $('#join_popup_outer').css('display', 'none');
    });
    $('.edit_footer>button:last-child').click(function(){
        $('#join_popup_outer').css('display', 'none');
    });

    //디스크(연결)이미지
    $('#img_btn2').click(function(){
        $('#join_img_th').css('display', '');
        $('#join_managed_th').css('display', 'none');
        $('#join_directlun_th').css('display', 'none');
        
        $('.join_img_td').css('display', '');
        $('.join_managed_td').css('display', 'none');
        $('.join_directlun_td').css('display', 'none');
        
        $('#join_new_nav>div').css('color', '').css('font-weight', '').css('border-bottom', '');
        $(this).css('color', 'rgb(35, 132, 243)').css('font-weight', '600').css('border-bottom', '2px solid rgb(35, 132, 243)');
    });
    //디스크(연결) 직접LUN
    $('#directlun_btn2').click(function(){
        $('#join_img_th').css('display', 'none');
        $('#join_managed_th').css('display', 'none');
        $('#join_directlun_th').css('display', '');
        
        $('.join_img_td').css('display', 'none');
        $('.join_managed_td').css('display', 'none');
        $('.join_directlun_td').css('display', '');
        
        $('#join_new_nav>div').css('color', '').css('font-weight', '').css('border-bottom', '');
        $(this).css('color', 'rgb(35, 132, 243)').css('font-weight', '600').css('border-bottom', '2px solid rgb(35, 132, 243)');
        $('#img_btn2').css('color', 'black').css('font-weight', '400').css('border-bottom', 'none');
    });
    //디스크(연결) 관리되는 블록
    $('#managed_block_btn2').click(function(){
        $('#join_img_th').css('display', 'none');
        $('#join_managed_th').css('display', '');
        $('#join_directlun_th').css('display', 'none');
        
        $('.join_img_td').css('display', 'none');
        $('.join_managed_td').css('display', '');
        $('.join_directlun_td').css('display', 'none');
        
        $('#join_new_nav>div').css('color', '').css('font-weight', '').css('border-bottom', '');
        $(this).css('color', 'rgb(35, 132, 243)').css('font-weight', '600').css('border-bottom', '2px solid rgb(35, 132, 243)');
        $('#img_btn2').css('color', 'black').css('font-weight', '400').css('border-bottom', 'none');
    });
    
    //  스냅샷 팝업(생성)   
    $('.snap_create_btn').click(function(){
        if($('.snap_create_outer').css('display') === 'none'){
            $('.snap_create_outer').css('display', 'block');
        } else {
            $('.snap_create_outer').css('display', 'none');
        }
    });
    $('.network_popup_header>button').click(function(){
        $('.snap_create_outer').css('display', 'none');
    });
    $('.edit_footer>button:last-child').click(function(){
        $('.snap_create_outer').css('display', 'none');
    });

    //  선호도그룹(새로만들기)   
    $('#pregroup_create_btn').click(function(){
        if($('.pregroup_create_outer').css('display') === 'none'){
            $('.pregroup_create_outer').css('display', 'block');
        } else {
            $('.pregroup_create_outer').css('display', 'none');
        }
    });
    $('.network_popup_header>button').click(function(){
        $('.pregroup_create_outer').css('display', 'none');
    });
    $('.edit_footer>button:last-child').click(function(){
        $('.pregroup_create_outer').css('display', 'none');
    });

    //  선호도레이블(새로만들기)   
    $('#lable_create_btn').click(function(){
        if($('.lable_create_outer').css('display') === 'none'){
            $('.lable_create_outer').css('display', 'block');
        } else {
            $('.lable_create_outer').css('display', 'none');
        }
    });
    $('.network_popup_header>button').click(function(){
        $('.lable_create_outer').css('display', 'none');
    });
    $('.edit_footer>button:last-child').click(function(){
        $('.lable_create_outer').css('display', 'none');
    });
    // 권한(추가)   
    $('#power_add_btn').click(function(){
        if($('.power_add_outer').css('display') === 'none'){
            $('.power_add_outer').css('display', 'block');
        } else {
            $('.power_add_outer').css('display', 'none');
        }
    });
    $('.network_popup_header>button').click(function(){
        $('.power_add_outer').css('display', 'none');
    });
    $('.edit_footer>button:last-child').click(function(){
        $('.power_add_outer').css('display', 'none');
    });
    
    //content_header_popup
    $('.content_header_popup_btn').click(function(event){
        event.stopPropagation(); // 이벤트 버블링 중지
        $('.content_header_popup').toggle();
    });
    // 바탕(body) 부분 클릭 이벤트
    $('body').click(function() {
        $('.content_header_popup').hide();
    });
    // 팝업창 내부 클릭 이벤트 (닫히지 않게 하기 위해)
    $('.content_header_popup').click(function(event) {
        event.stopPropagation(); // 이벤트 버블링 중지
    });



    //팝업창
    //일반버튼
    $('#common_btn').click(function() {
        $('#common_outer').css('display', 'block');
        $('#system_outer').css('display', 'none');
        $('#host_outer').css('display', 'none');
        $('#console_outer').css('display', 'none');
        $('#ha_mode_outer').css('display', 'none');
        $('#res_alloc_outer').css('display', 'none');
        $('#boot_outer').css('display', 'none');
        $('#preference_outer').css('display', 'none');
    });
    //시스템버튼
    $('#system_btn').click(function() {
        $('#common_outer').css('display', 'none');
        $('#system_outer').css('display', 'block');
        $('#host_outer').css('display', 'none');
        $('#console_outer').css('display', 'none');
        $('#ha_mode_outer').css('display', 'none');
        $('#res_alloc_outer').css('display', 'none');
        $('#boot_outer').css('display', 'none');
        $('#preference_outer').css('display', 'none');
    });
    //콘솔버튼
    $('#console_btn').click(function() {
        $('#common_outer').css('display', 'none');
        $('#system_outer').css('display', 'none');
        $('#host_outer').css('display', 'none');
        $('#console_outer').css('display', 'block');
        $('#ha_mode_outer').css('display', 'none');
        $('#res_alloc_outer').css('display', 'none');
        $('#boot_outer').css('display', 'none');
        $('#preference_outer').css('display', 'none');
    });
    //호스트버튼
    $('#host_btn').click(function() {
        $('#common_outer').css('display', 'none');
        $('#system_outer').css('display', 'none');
        $('#host_outer').css('display', 'block');
        $('#console_outer').css('display', 'none');
        $('#ha_mode_outer').css('display', 'none');
        $('#res_alloc_outer').css('display', 'none');
        $('#boot_outer').css('display', 'none');
        $('#preference_outer').css('display', 'none');
    });
     //고가용성 버튼
    $('#ha_mode_btn').click(function() {
        $('#common_outer').css('display', 'none');
        $('#system_outer').css('display', 'none');
        $('#host_outer').css('display', 'none');
        $('#console_outer').css('display', 'none');
        $('#ha_mode_outer').css('display', 'block');
        $('#res_alloc_outer').css('display', 'none');
        $('#boot_outer').css('display', 'none');
        $('#preference_outer').css('display', 'none');
    });
    //리소스할당 버튼
    $('#res_alloc_btn').click(function() {
        $('#common_outer').css('display', 'none');
        $('#system_outer').css('display', 'none');
        $('#host_outer').css('display', 'none');
        $('#console_outer').css('display', 'none');
        $('#ha_mode_outer').css('display', 'none');
        $('#res_alloc_outer').css('display', 'block');
        $('#boot_outer').css('display', 'none');
        $('#preference_outer').css('display', 'none');
    });
    //부트 옵션버튼
    $('#boot_btn').click(function() {
        $('#common_outer').css('display', 'none');
        $('#system_outer').css('display', 'none');
        $('#host_outer').css('display', 'none');
        $('#console_outer').css('display', 'none');
        $('#ha_mode_outer').css('display', 'none');
        $('#res_alloc_outer').css('display', 'none');
        $('#boot_outer').css('display', 'block');
        $('#preference_outer').css('display', 'none');
    });
    //선호도 버튼
    $('#preference_btn').click(function() {
    $('#common_outer').css('display', 'none');
    $('#system_outer').css('display', 'none');
    $('#host_outer').css('display', 'none');
    $('#console_outer').css('display', 'none');
    $('#ha_mode_outer').css('display', 'none');
    $('#res_alloc_outer').css('display', 'none');
    $('#boot_outer').css('display', 'none');
    $('#preference_outer').css('display', 'block');
    });

   // footer 팝업창
    $('.footer>button').click(function(){
        if($('.footer_content').css('display') === 'none'){
            $('.footer_content').css('display', 'block');
        } else {
            $('.footer_content').css('display', 'none');
        }
    });

    //footer 최근작업,경보 선택
    $(".footer>div>a").click(function(){
        // 클릭한 div의 현재 글자색가져오기
        var currentColor = $(this).css("color");
        // 만약 현재 글자색이 회색이라면
        if (currentColor === "black") {
            // 계속 검정색유지
            $(this).css("color", "black");
        } else { // 만약 회색이아니라면
            $(".footer>div>a").css('color','#4F4F4F');
            $(".footer>div>a").css('border-bottom','none');
            $(this).css("color", "black");
            $(this).css("border-bottom", "1px solid blue");
        }
    });
    
});

//!!스토리지 부분----------------------------------------------------
$(function() {
    $('#storage_section .content_header_left div').click(function() {
        var index = $(this).index();
    
        // 모든 섹션을 숨기고 클릭된 섹션만 보이기
        var sections = [
            '.section_content_outer',
            '#storage_domain_outer',
            '#storage_volume_outer',
            '#storage_storage_outer',
            '#storage_logic_outer',
            '#storage_cluster_outer',
            '#storage_right_outer'
        ];
        
        // 선택된 섹션을 제외하고 모두 숨기기
        sections.forEach(function(section, i) {
            if (i === index) {
                $(section).show();
            } else {
                $(section).hide();

                
            }
        });
    
        // 현재 활성화된 섹션의 content_header_left에서 active 클래스 제거
        $('#storage_section .content_header_left div').removeClass('active');
    
        // 클릭된 요소에 active 클래스 추가
        $(this).addClass('active');
    });
    
    // 처음 로딩 시 첫 번째 섹션을 기본으로 활성화
    setTimeout(function() {
        $('#storage_section .content_header_left div').first().trigger('click');
    }, 0);


    //스토리지 디스크(새로만들기) 팝업창
    $('#storage_disk_new_btn').click(function(){
        if($('.storage_disk_new_outer').css('display') === 'none'){
            $('.storage_disk_new_outer').css('display', 'block');
        } else {
            $('.storage_disk_new_outer').css('display', 'none');
        }
    });
    $('.network_popup_header>button').click(function(){
        $('.storage_disk_new_outer').css('display', 'none');
    });
    $('.edit_footer>button:last-child').click(function(){
        $('.storage_disk_new_outer').css('display', 'none');
    });

    // 스토리지 디스크(새로만들기->이미지) 팝업창
    $('#storage_img_btn').click(function(){
        $('#storage_directlun_outer').css('display', 'none');
        $('.storage_disk_new_outer .disk_new_img').css('display', 'block');
        $('#storage_managed_outer').css('display', 'none');
    });
    //스토리지 디스크(새로만들기->직접Lun) 팝업창
    $('#storage_directlun_btn').click(function(){
        $('#storage_directlun_outer').css('display', 'block');
        $('.storage_disk_new_outer .disk_new_img').css('display', 'none');
        $('#storage_managed_outer').css('display', 'none');
    });


    //스토리지 디스크(새로만들기->관리되는 블록) 팝업창
    $('#storage_managed_btn').click(function(){
        $('#storage_directlun_outer').css('display', 'none');
        $('.storage_disk_new_outer .disk_new_img').css('display', 'none');
        $('#storage_managed_outer').css('display', 'flex');
    });

    //스토리지 디스크(업로드) 팝업창
    $('#storage_disk_upload').click(function(){
        if($('.storage_disk_upload_outer').css('display') === 'none'){
            $('.storage_disk_upload_outer').css('display', 'block');
        } else {
            $('.storage_disk_upload_outer').css('display', 'none');
        }
    });
    $('.network_popup_header>button').click(function(){
        $('.storage_disk_upload_outer').css('display', 'none');
    });
    $('.edit_footer>button:last-child').click(function(){
        $('.storage_disk_upload_outer').css('display', 'none');
    });

    //스토리지 도메인(새로운 도메인) 팝업창
    $('#new_domain_btn').click(function(){
        if($('.storage_domain_new_outer').css('display') === 'none'){
            $('.storage_domain_new_outer').css('display', 'block');
        } else {
            $('.storage_domain_new_outer').css('display', 'none');
        }
    });
    $('.network_popup_header>button').click(function(){
        $('.storage_domain_new_outer').css('display', 'none');
    });
    $('.edit_footer>button:last-child').click(function(){
        $('.storage_domain_new_outer').css('display', 'none');
    });

    // 스토리지 도메인(새로운도메인 -> 사용자정의 연결매개변수 히든박스)
    $('#domain_hidden_box_btn').click(function(){
        if($('#domain_hidden_box').css('display') === 'none'){
            $('#domain_hidden_box').css('display', 'block');
        } else {
            $('#domain_hidden_box').css('display', 'none');
        }
    });
    // 스토리지 도메인(새로운도메인 -> 고급매개변수 히든박스)
    $('#domain_hidden_box_btn2').click(function(){
        if($('#domain_hidden_box2').css('display') === 'none'){
            $('#domain_hidden_box2').css('display', 'block');
        } else {
            $('#domain_hidden_box2').css('display', 'none');
        }
    });

    //스토리지 도메인(도메인 가져오기) 팝업창
    $('#get_domain_btn').click(function(){
        if($('.storage_domain_get_outer').css('display') === 'none'){
            $('.storage_domain_get_outer').css('display', 'block');
        } else {
            $('.storage_domain_get_outer').css('display', 'none');
        }
    });
    $('.network_popup_header>button').click(function(){
        $('.storage_domain_get_outer').css('display', 'none');
    });
    $('.edit_footer>button:last-child').click(function(){
        $('.storage_domain_get_outer').css('display', 'none');
    });
    //스토리지 도메인(도메인 관리) 팝업창
    $('#administer_domain_btn').click(function(){
        if($('.storage_domain_administer_outer').css('display') === 'none'){
            $('.storage_domain_administer_outer').css('display', 'block');
        } else {
            $('.storage_domain_administer_outer').css('display', 'none');
        }
    });
    $('.network_popup_header>button').click(function(){
        $('.storage_domain_administer_outer').css('display', 'none');
    });
    $('.edit_footer>button:last-child').click(function(){
        $('.storage_domain_administer_outer').css('display', 'none');
    });

    // 스토리지 볼륨(새로만들기)팝업
    $('#storage_volume_new_btn').click(function(){
        if($('.storage_volume_new_outer').css('display') === 'none'){
            $('.storage_volume_new_outer').css('display', 'block');
        } else {
            $('.storage_volume_new_outer').css('display', 'none');
        }
    });
    $('.network_popup_header>button').click(function(){
        $('.storage_volume_new_outer').css('display', 'none');
    });
    $('.edit_footer>button:last-child').click(function(){
        $('.storage_volume_new_outer').css('display', 'none');
    });

    // 스토리지 볼륨(스냅샷)팝업
    $('#storage_volume_snap_btn').click(function(){
        if($('.storage_volume_snap_outer').css('display') === 'none'){
            $('.storage_volume_snap_outer').css('display', 'block');
        } else {
            $('.storage_volume_snap_outer').css('display', 'none');
        }
    });
    $('.network_popup_header>button').click(function(){
        $('.storage_volume_snap_outer').css('display', 'none');
    });
    $('.edit_footer>button:last-child').click(function(){
        $('.storage_volume_snap_outer').css('display', 'none');
    });
    // 스토리지 볼륨(스냅샷 옵션박스)
    $('#storage_volume_option_boxbtn').click(function(){
        if($('.storage_volume_option_box').css('display') === 'none'){
            $('.storage_volume_option_box').css('display', 'block');
        } else {
            $('.storage_volume_option_box').css('display', 'none');
        }
    });
//!!네트워크 부분---------------------------------------------------------
// 네트워크(새로만들기 -> 일반) 팝업
$('#network_new_btn').click(function(){
    if ($('.network_new_outer').css('display') === 'none') {
        $('.network_new_outer').css('display', 'block');
        $('#network_new_common_form').show();
        $('#network_new_cluster_form').hide();
        $('#network_new_vnic_form').hide();
        $('#network_new_common_btn').addClass('active');
        $('#network_new_cluster_btn').removeClass('active');
        $('#network_new_vnic_btn').removeClass('active');
    } else {
        $('.network_new_outer').css('display', 'none');
    }
});

$('.network_popup_header>button').click(function(){
    $('.network_new_outer').css('display', 'none');
});

$('.edit_footer>button:last-child').click(function(){
    $('.network_new_outer').css('display', 'none');
});

// 네트워크(새로만들기 -> 일반) 폼
$('#network_new_common_btn').click(function(){
    $('#network_new_common_form').show();
    $('#network_new_cluster_form').hide();
    $('#network_new_vnic_form').hide();
    $(this).addClass('active');
    $('#network_new_cluster_btn').removeClass('active');
    $('#network_new_vnic_btn').removeClass('active');
});

// 네트워크(새로만들기 -> 클러스터) 폼
$('#network_new_cluster_btn').click(function(){
    $('#network_new_cluster_form').show();
    $('#network_new_common_form').hide();
    $('#network_new_vnic_form').hide();
    $(this).addClass('active');
    $('#network_new_common_btn').removeClass('active');
    $('#network_new_vnic_btn').removeClass('active');
});

// 네트워크(새로만들기 -> vNIC 프로파일) 폼
$('#network_new_vnic_btn').click(function(){
    $('#network_new_vnic_form').show();
    $('#network_new_common_form').hide();
    $('#network_new_cluster_form').hide();
    $(this).addClass('active');
    $('#network_new_common_btn').removeClass('active');
    $('#network_new_cluster_btn').removeClass('active');
});

    //  네트워크(가져오기) 팝업
    $('#network_bring_btn').click(function(){
        if($('.network_bring_outer').css('display') === 'none'){
            $('.network_bring_outer').css('display', 'block');
        } else {
            $('.network_bring_outer').css('display', 'none');
        }
    });
    $('.network_popup_header>button').click(function(){
        $('.network_bring_outer').css('display', 'none');
    });
    $('.edit_footer>button:last-child').click(function(){
        $('.network_bring_outer').css('display', 'none');
    });


//!!설정/세팅 부분---------------------------------------------------------
    //  설정(설정) 팝업
    $('#setting_miniset_btn').click(function(){
        if($('.setting_setting_outer').css('display') === 'none'){
            $('.setting_setting_outer').css('display', 'block');
            
        } else {
            $('.setting_setting_outer').css('display', 'none');
        }
    });
    $('.network_popup_header>button').click(function(){
        $('.setting_setting_outer').css('display', 'none');
    });
    $('.edit_footer>button:last-child').click(function(){
        $('.setting_setting_outer').css('display', 'none');
    });


    //  설정(설정) 팝업(역할,시스템권한,스케줄링정책 등)
    showForm('part');

    // 폼을 표시하는 함수
    function showForm(type) {
        const forms = ['part', 'system', 'schedule', 'instant', 'mac'];
        forms.forEach(function(form) {
            if (form === type) {
                $(`#setting_${form}_form`).show();
                $(`#setting_${form}_btn`).addClass('active').css('color', 'blue');
            } else {
                $(`#setting_${form}_form`).hide();
                $(`#setting_${form}_btn`).removeClass('active').css('color', '');
            }
        });
    }

    // 설정 버튼 클릭 핸들러
    ['part', 'system', 'schedule', 'instant', 'mac'].forEach(function(type) {
        $(`#setting_${type}_btn`).click(function() {
            showForm(type);
        });
    });

   
    // 세팅 목록별 section
    // 활성 사용자 세션
    $('#setting_normal_btn').click(function(){
        $('#setting_user_section').css('display','none');
        $('#setting_section').css('display','block');
    });
    // 사용자버튼
    $('#setting_user_btn').click(function(){
        $('#setting_user_section').css('display','block');
        $('#setting_section').css('display','none');
    });
    
    


    


    // 팝업 닫기 버튼 클릭 핸들러
    $('.network_popup_header>button').click(function(){
        $('.setting_setting_outer').css('display', 'none');
    });

    // 팝업 닫기 (취소 버튼 클릭 핸들러)
    $('.edit_footer>button:contains("취소")').click(function(){
        $('.setting_setting_outer').css('display', 'none');
    });

    // 역할 새로만들기
    $('#setting_part_new_btn').click(function(){
        if($('.setting_part_new_outer').css('display') === 'none'){
            $('.setting_part_new_outer').css('display', 'block');
            
        } else {
            $('.setting_part_new_outer').css('display', 'none');
        }
    });
    $('.network_popup_header>button').click(function(){
        $('.setting_part_new_outer').css('display', 'none');
    });
    $('.edit_footer>button:last-child').click(function(){
        $('.setting_part_new_outer').css('display', 'none');
    });

    // 시스템권한 추가
    $('#setting_system_add_btn').click(function(){
        if($('.setting_system_new_outer').css('display') === 'none'){
            $('.setting_system_new_outer').css('display', 'block');
            
        } else {
            $('.setting_system_new_outer').css('display', 'none');
        }
    });
    $('.network_popup_header>button').click(function(){
        $('.setting_system_new_outer').css('display', 'none');
    });
    $('.edit_footer>button:last-child').click(function(){
        $('.setting_system_new_outer').css('display', 'none');
    });

    // 스케줄링 정책->새로만들기 팝업
    $('#setting_schedule_new_btn').click(function(){
        if($('.setting_schedule_new_outer').css('display') === 'none'){
            $('.setting_schedule_new_outer').css('display', 'block');
            
        } else {
            $('.setting_schedule_new_outer').css('display', 'none');
        }
    });
    $('.network_popup_header>button').click(function(){
        $('.setting_schedule_new_outer').css('display', 'none');
    });
    $('.edit_footer>button:last-child').click(function(){
        $('.setting_schedule_new_outer').css('display', 'none');
    });

    // MAC주소 풀->새로만들기 팝업
    $('#setting_mac_new_btn').click(function(){
        if($('.setting_mac_new_outer').css('display') === 'none'){
            $('.setting_mac_new_outer').css('display', 'block');
            
        } else {
            $('.setting_mac_new_outer').css('display', 'none');
        }
    });
    $('.network_popup_header>button').click(function(){
        $('.setting_mac_new_outer').css('display', 'none');
    });
    $('.edit_footer>button:last-child').click(function(){
        $('.setting_mac_new_outer').css('display', 'none');
    });

    // MAC주소 풀->편집 팝업
    $('#setting_mac_edit_btn').click(function(){
        if($('.setting_mac_edit_outer').css('display') === 'none'){
            $('.setting_mac_edit_outer').css('display', 'block');
            
        } else {
            $('.setting_mac_edit_outer').css('display', 'none');
        }
    });
    $('.network_popup_header>button').click(function(){
        $('.setting_mac_edit_outer').css('display', 'none');
    });
    $('.edit_footer>button:last-child').click(function(){
        $('.setting_mac_edit_outer').css('display', 'none');
    });

    // 설정->사용자->추가
    $('#set_user_add_btn').click(function(){
        if($('.setting_user_new_outer').css('display') === 'none'){
            $('.setting_user_new_outer').css('display', 'block');
            
        } else {
            $('.setting_user_new_outer').css('display', 'none');
        }
    });
    $('.network_popup_header>button').click(function(){
        $('.setting_user_new_outer').css('display', 'none');
    });
    $('.edit_footer>button:last-child').click(function(){
        $('.setting_user_new_outer').css('display', 'none');
    });
});
//!!content_detail_section 부분----------------------------------------------------
$(function() {
    // 모든 세부 섹션을 숨기기
    $('#detail_datacenter_outer, #detail_machine_outer, #detail_template_outer, #detail_disk_outer, #detail_snapshot_outer, #detail_rent_outer, #detail_profile_outer,#detail_event_outer,#detail_right_outer').hide();
    
    // 기본적으로 '일반' 섹션 활성화
    $('.content_outer').show(); // '일반'에 해당하는 div를 보여줌
    $('.content_detail_section .content_header_left .active').removeClass('active'); // 다른 모든 활성화 상태 제거
    $('.content_detail_section .content_header_left div').first().addClass('active'); // '일반' div에 활성화 클래스 추가

    // 탭 클릭 이벤트 처리
    $('.content_detail_section .content_header_left div').click(function() {
        var index = $(this).index();
        var sections = [
            '.content_outer',             // 일반
            '#detail_datacenter_outer',   // 데이터 센터
            '#detail_machine_outer',      // 가상머신
            '#detail_template_outer',     // 템플릿
            '#detail_disk_outer',         // 디스크
            '#detail_snapshot_outer',     // 디스크 스냅샷
            '#detail_rent_outer',         // 임대
            '#detail_profile_outer',       // 디스크 프로파일
            '#detail_event_outer',
            '#detail_right_outer'
        ];

        // 모든 섹션을 숨기고, 선택된 섹션만 보이게 하기
        sections.forEach(function(section, i) {
            if (i === index) {
                $(section).show();
            } else {
                $(section).hide();  
            }
        });

        // 현재 활성화된 섹션의 content_header_left에서 active 클래스 제거
        $('.content_detail_section .content_header_left div').removeClass('active');

        // 클릭된 요소에 active 클래스 추가
        $(this).addClass('active');
    });
});
