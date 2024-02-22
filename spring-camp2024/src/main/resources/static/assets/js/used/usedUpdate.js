/**
 * usedUpdate.js파일
 */

$(function(){
		var fileNameList = $("#fileNameList").val();
		var fileNameArray = fileNameList.split(',');
		console.log(fileNameArray);
		for (var i = 0; i < fileNameArray.length; i++) {
	        console.log(fileNameArray[i]);
	    var hiddenInput = $('<input>').attr({
	        type: 'hidden',
	        name: 'file' + (i + 1), // 파일명을 file1, file2, file3으로 할당
	        value: fileNameArray[i],// 파일명을 hidden input의 값으로 설정
	        id : 'file' + (i + 1)
	    });
		  $('form[name="usedUpdateFrm"]').append(hiddenInput); // hidden input을 폼에 추가
			//usedUpdateFrm.submit();
			};
		
		
	
	/*제목,내용,가격 막기*/
	$("#u_saveBtn").click(function(){
		if($("#used_btitle").val().length<1){
			alert("제목을 입력하셔야 합니다.");
			$(".used_input1").focus();
			return false;
		};
		if($(".used_bcontent").val().length<1){
			alert("내용을 작성하셔야 합니다.");
			$(".used_bcontent").focus();
			return false;
		};	
		var uPrice= $("#u_bprice").val().trim();
		 if(uPrice ===""){
	            alert("가격을 작성하셔야 합니다.");
	            $(".used_input1").focus();
	            return false;
		};
		
		var selectedValue=$("#used_trade").val();
		if(selectedValue === null || selectedValue===""){
			alert("유형을 선택하셔야 합니다.");
			return false;
		};
		
		usedUpdateFrm.submit();
  	
	});//click
	
/*사진 미리보기*/
$(document).on('change', '.u_files_input', function() {
    var fileInput = this;
    var file = fileInput.files[0]; // 첫 번째 파일만 선택

    if (file) {
        var reader = new FileReader();
        var filePreviewContainer = $(fileInput).prev('.file-preview-container');
        filePreviewContainer.empty(); // 기존에 표시된 미리보기 초기화

        reader.onloadend = function () {
            // 파일 미리보기 표시
            var previewItem = $('<div class="file-preview-item"></div>');
            previewItem.append('<img src="' + reader.result + '" alt="File Preview" class="file-preview-img">');
            filePreviewContainer.append(previewItem);
        };

        reader.readAsDataURL(file);
    }
});

	/*가격 숫자만 입력가능*/
    // 입력란에 입력이 발생할 때 이벤트 처리
     $('#u_bprice').on('input', function() {
        // 입력된 값 가져오기
        var value = $(this).val();
        // 숫자 이외의 문자가 있는지 확인하는 정규식
        var regex = /^[0-9]*$/;
        // 숫자와 일치하지 않는 문자가 입력된 경우
        if (!regex.test(value)) {
            // 입력된 값에서 숫자 이외의 문자를 모두 제거하여 다시 입력란에 설정
            $(this).val(value.replace(/\D/g, ''));
            console.log("Invalid input");
        }
});      
	
/*usedUpdate*/	   
	/* 유형(중고거래, 캠핑장양도) => selected */
	var selectElement = $("#used_trade");
	var options = selectElement.find("option");
	
	var Ou_btype = $(".update_btype").data("btype");
	
	options.each(function(){
		if($(this).val() == Ou_btype){
			$(this).prop("selected",true);
		}
	});//options

	/* 거래장소 => selected */
	var local_options = $("#used_region").find("option");
	var Ou_blocal = $(".used_region").data("blocal");
	
	local_options.each(function(){
		if($(this).val() == Ou_blocal){
			$(this).prop("selected",true);
		}
		
	});
	


});//jquery