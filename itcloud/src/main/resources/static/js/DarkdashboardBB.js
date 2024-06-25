$(function(){

        // aside 팝업(가상머신버튼)
        $('#aside_popup_machine_btn').click(function(){
            $('#section').css('display','block');
            $('#storage_section').css('display','none');
            $('#network_section').css('display','none');
            $('#setting_section').css('display','none');

            $('#virtual_machine_chart').css('display','block');
            $('#storage_chart').css('display','none');
            $('#network_chart').css('display','none');
            $('#setting_chart').css('display','none');
        if ($('#aside_popup').css('display') === 'none' || $('#aside_popup_machine_btn').css('background-color') !== 'rgb(255, 255, 0)') {
            $('#aside_popup').css('display', 'block');
            $('#aside_outer').css('width','20%');
            $('#aside_popup_machine_btn').css('background-color', 'yellow'); // 배경색을 노란색으로 변경
            $('#aside_popup_storage_btn').css('background-color', ''); // 다른 버튼 초기화
        } else {
            $('#aside_popup').css('display', 'none');
            $('#aside_outer').css('width','3%');
            $('#aside_popup_machine_btn').css('background-color', ''); // 배경색 초기화
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
    

    // aside 팝업(스토리지 버튼)
    $('#aside_popup_storage_btn').click(function(){      
            $('#section').css('display','none');
            $('#storage_section').css('display','block');
            $('#network_section').css('display','none');
            $('#setting_section').css('display','none');

            $('#virtual_machine_chart').css('display','none');
            $('#storage_chart').css('display','block');
            $('#network_chart').css('display','none');
            $('#setting_chart').css('display','none');
        if ($('#aside_popup').css('display') === 'none' || $('#aside_popup_storage_btn').css('background-color') !== 'rgb(255, 255, 0)') {
            $('#aside_popup').css('display', 'block');
            $('#aside_outer').css('width','20%');
            $('#aside_popup_storage_btn').css('background-color', 'yellow'); // 배경색을 노란색으로 변경
            $('#aside_popup_machine_btn').css('background-color', ''); // 다른 버튼 초기화

          
         } else {
             $('#aside_popup').css('display', 'none');
             $('#aside_outer').css('width','3%');
             $('#aside_popup_storage_btn').css('background-color', ''); // 배경색 초기화
         }
    });
    // aside 팝업(네트워크 버튼)
    $('#aside_popup_network_btn').click(function(){
        $('#section').css('display','none');
        $('#storage_section').css('display','none');
        $('#network_section').css('display','block');
        $('#setting_section').css('display','none');

        $('#virtual_machine_chart').css('display','none');
        $('#storage_chart').css('display','none');
        $('#network_chart').css('display','block');
        $('#setting_chart').css('display','none');
        if ($('#aside_popup').css('display') === 'none' || $('#aside_popup_network_btn').css('background-color') !== 'rgb(255, 255, 0)') {
            $('#aside_popup').css('display', 'block');
            $('#aside_outer').css('width','20%');
            $('#aside_popup_network_btn').css('background-color', 'yellow'); // 배경색을 노란색으로 변경
            $('#aside_popup_machine_btn').css('background-color', ''); // 다른 버튼 초기화

          
         } else {
             $('#aside_popup').css('display', 'none');
             $('#aside_outer').css('width','3%');
             $('#aside_popup_network_btn').css('background-color', ''); // 배경색 초기화
         }
    });
}); 