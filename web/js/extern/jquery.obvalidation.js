/**
 * Openbase JQuery Validation Plug-in
 * 
 * <pre>
 * $(target).validate(options)
 * 	- target: value를 가진 객체(주로 input)
 * 	- options: 유효성 검사 조건
 * 	- 조건에 대해 하나 이상의 유효성 문제 발생시 경고 후 검사 중지
 * 
 * $.validate([options(with target), ...])
 * 	- 각 배열에 포함된 options를 통해 validate 수행(target 또한 options에 포함)
 * 	- 포함된 모든 조건 검사 중 하나 이상의 유효성 문제 발생시 경고 후 검사 중지
 * 
 * options
 * 	name(String): 메세지에 포함될 개체명(ex: ${name}은 필수 입력 항목입니다.)
 * 	checked(boolean): 유효성 검사를 수행할지 여부(체크박스 상태에 따른 유효성 검사 수행 등에 활용)
 * 	type(String): 데이터 형식 검사
 * 		- 지원 형식목록: ko, en, special, number, email, ip, mac, phonenum
 * 		- 지원 연산자: !type - NOT 연산, type&type,... - AND 연산
 * 	required(boolean): 필수값 검사(빈 값 허용 안함)
 * 	min(Number): 최소값(숫자만 해당)
 * 	max(Number): 최대값(숫자만 해당)
 * 	range([Number, Number]): 최소~최대값(숫자만 해당)
 * 	minLength(Number): 최소길이
 * 	maxLength(Number): 최대길이
 * 	lengthRange([Number, Number]): 최소~최대길이
 * 	equals(String): 값 일치 검사(ex: 비밀번호, 비밀번호 확인 등)
 * 	regExp(reqExp): 전용 정규식을 통한 검사
 * 	msg(String): 커스텀 오류메세지 설정(regExp 전용)
 * 		- ${name}은 name 옵션으로 치환된다.
 * 		- ${condition}은 regExp로 치환된다.
 * </pre>
 * 
 * @import jQuery 1.9.1
 * @author sw.jung
 */

/* Patch Note
 * 14.07.22
 * - name 타입 추가
 * - en_name 타입 추가
 * - type regExp 오류 수정(정상적으로 검사 안되었음)
 * 
 * 14.07.23
 * - not 연산자 표현 수정
 * - id 타입 추가
 * 
 * 14.07.28
 * - mac_part 타입 추가(매세지는 MAC과 동일)
 */

// Default Messages
var MSG_VALID_DEFAULT_NAME = "target";
var MSG_VALID_EMPTY = "${name} is required.";
var MSG_VALID_MISSING_TYPE = "${name} is missing type.(available type: ${condition})";
var MSG_VALID_TYPE_DEFAULT = "could not start/end with space";
var MSG_VALID_TYPE_PREFIX_NOT = "not ";
var MSG_VALID_TYPE_KO = "${not}korean";
var MSG_VALID_TYPE_EN = "${not}english";
var MSG_VALID_TYPE_SPECIAL = "${not}special character";
var MSG_VALID_TYPE_NUMBER = "${not}number";
var MSG_VALID_TYPE_EMAIL = "${not}email";
var MSG_VALID_TYPE_IP = "${not}ip";
var MSG_VALID_TYPE_MAC = "${not}mac address";
var MSG_VALID_TYPE_PHONENUM = "${not}phone number";
var MSG_VALID_TYPE_NAME = "${not}allowed special character: #@-_.,()";
var MSG_VALID_TYPE_EN_NAME = "${not}english and ${not} allowed special character: #@-_.,()";
var MSG_VALID_TYPE_ID = "${not} id(start with alphabet, allowed special character .-_)";
var MSG_VALID_TOO_SMALL = "${name} is too small.(min: ${condition})";
var MSG_VALID_TOO_LARGE = "${name} is too large.(max: ${condition})";
var MSG_VALID_OUT_OF_RANGE = "${name} is out of range.(range: ${condition})";
var MSG_VALID_TO_SHORT = "${name} is too short.(min length: ${condition})";
var MSG_VALID_TO_LONG = "${name} is too long.(min length: ${condition})";
var MSG_VALID_OUT_OF_LENGTH_RANGE = "${name} is out of length range.(length range: ${condition})";
var MSG_VALID_NOT_EQUAL = "${name} is not valid.(equal: ${condition})";
var MSG_VALID_MISS_MATCH = "${name} is miss matching.(match: ${condition})";

var MSG_ERROR_VALIDATION_FAIL = "validation failed.";

//	커스텀 메세지 적용법: main.jsp에서 프로퍼티 로드시 위 메세지를 치환
//	jQuery.i18n.properties({
//	    name:'Messages',
//	    path:'bundle/',
//	    mode:'both',
//	    language:langCode, 
//	    callback: function()
//	    {
//	    	MSG_VALID_EMPTY = jQuery.i18n.prop("MSG_VALID_EMPTY");
//	    	MSG_VALID_MISSING_TYPE = jQuery.i18n.prop("MSG_VALID_MISSING_TYPE");
//	    	...
//	    }
//	});

(function($)
{
	// type 검사시 참조하는 객체, 형식에 맞춰 정의하면 type으로 쓸 수 있다.
	var validationTypes =
	{
		ko:
		{
			regExp: /^[가-힣]*$/,
			typeMsg: 'MSG_VALID_TYPE_KO'
		},
		en:
		{
			regExp:  /^[a-zA-Z]*$/,
			typeMsg: 'MSG_VALID_TYPE_EN'
		},
		special:
		{
			regExp: /[^a-z^A-Z^0-9^가-힣\., ]+$/,
			typeMsg: 'MSG_VALID_TYPE_SPECIAL'
		},
		number:
		{
			regExp: /^[0-9]*$/,
			typeMsg: 'MSG_VALID_TYPE_NUMBER'
		},
		email:
		{
			regExp: /^[-a-z0-9~!$%^&*_=+}{\'?]+(\.[-a-z0-9~!$%^&*_=+}{\'?]+)*@([a-z0-9_][-a-z0-9_]*(\.[-a-z0-9_]+)*\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}))(:[0-9]{1,5})?$/i,
			typeMsg: 'MSG_VALID_TYPE_EMAIL'
		},
		ip:
		{
			regExp: /^((25[0-5]|2[0-4]\d|1\d{2}|[1-9]\d|\d)\.){3}(25[0-5]|2[0-4]\d|1\d{2}|[1-9]\d|\d)$/,
			typeMsg: 'MSG_VALID_TYPE_IP'
		},
		ip_part: // TODO: 부분구현(클래스당 000~999까지 가능, 0~255까지만 가능하게 변경할 것)
		{
			regExp: /^([0-9]{1,3}\.{1}){0,3}[0-9]{0,3}$/,
			typeMsg: 'MSG_VALID_TYPE_IP'
		},
		phonenum:
		{
			regExp: /^([0-9]{2,3})([0-9]{3,4})([0-9]{4})$/,
			typeMsg: 'MSG_VALID_TYPE_PHONENUM'
		},
		mac:
		{
			regExp: /^([a-fA-F0-9]{2}:){5}([a-fA-F0-9]{2})$/,
			typeMsg: 'MSG_VALID_TYPE_MAC'
		},
		mac_part:
		{
			regExp: /^([a-fA-F0-9]{2}:{1}){0,5}[a-fA-F0-9]{0,2}$/,
			typeMsg: 'MSG_VALID_TYPE_MAC'
		},
		name:
		{
			regExp: /^[a-zA-Z0-9가-힣#@\-_\.,\(\) ]*$/,
			typeMsg: 'MSG_VALID_TYPE_NAME'
		},
		en_name:
		{
			regExp: /^[a-zA-Z0-9#@\-_\.,\(\)\/ ]*$/,
			typeMsg: 'MSG_VALID_TYPE_EN_NAME'
		},
		f5_name:
		{
			regExp: /^[a-zA-Z0-9@\-_\.,\(\)\/ ]*$/,
			typeMsg: 'MSG_VALID_TYPE_F5_NAME'
		},
		id:
		{
			regExp: /^[A-Za-z][a-zA-Z0-9\._\-]*?$/,
			typeMsg: 'MSG_VALID_TYPE_ID'
		}
	};
	
//	$.validator = // 전역 객체로 사용하고자 할 때
	var validator = // 내부처리 전용(private)
	{
		// 값이 지정한 형식에 유효하다면 true
		typeTest: function(value, type)
		{
			type = type.replace("!", "").trim(); // NOT 연산자 제거
			
			return validationTypes[type] ? validationTypes[type].regExp.test(value) : true;
		},
		// 검사에 포함된 형식들에 대한 메세지
		getTypeMsg: function(type)
		{
			if (!type)
				return MSG_VALID_TYPE_DEFAULT;
			var types = type.toLowerCase().split(/[&]/); // &연산자 단위별로 분리
			var type;
			var typeMsg = "";
			for (var i = 0; i < types.length; i ++)
			{
				type = types[i].replace("!", "").trim(); // 연산자 제거한 형태
				typeMsg += validationTypes[type] ? // 존재하는 타입이라면
						eval(validationTypes[type].typeMsg).replace("${not}", // 메세지에서 ${not} 구문 치환
								types[i].indexOf("!") >= 0 ? MSG_VALID_TYPE_PREFIX_NOT : "") + "," : ""; // !연산자에 따라 다름
			}
			
			return typeMsg.substring(0, typeMsg.lastIndexOf(",")); // 마지막 , 제거
		},
		isValidType: function(value, regExp)
		{
			var types = regExp.toLowerCase().split(/[&]/);
			var result = true;
			
			for (var i = 0; i < types.length; i ++)
			{
				// 조건에 맞지 않는 형식 발생시
				if ((types[i].indexOf("!") < 0 && !this.typeTest(value, types[i]))
						|| (types[i].indexOf("!") >= 0 && this.typeTest(value, types[i])))
				{
					result = false;
					break;
				}
			}
			return result;
		},
		validation: function(target, options) // 실제 호출되는 함수
		{
			var value = target.value;
			var name = options.name ? options.name : MSG_VALID_DEFAULT_NAME;
			var type = options.type;
			var required = options.required;
			var min = options.min;
			var max = options.max;
			var range = options.range;
			var minLength = options.minLength;
			var maxLength = options.maxLength;
			var lengthRange = options.lengthRange;
			var equals = options.equals;
			var regExp = options.regExp;
			var msg = options.msg;
			
			// 공백문자 제거(제거 안함!)
//			if (value != undefined)
//				value = value.trim();
			
			// 필수값 검사
			if (required && (value == undefined || value == ""))
				return this.formatMsg(MSG_VALID_EMPTY, name);

			// require가 아니면서, 값이 없을 경우 유효성 검사 skip
			if (value == undefined || value == "")
				return undefined;

			// 지정 type 검사(ex: ip, mac, number, ...)
			if (value.trim().length != value.length || (type != undefined && !this.isValidType(value, type)))
				return this.formatMsg(MSG_VALID_MISSING_TYPE, name, this.getTypeMsg(type));
			
			// 최소값 검사(숫자)
			if (!isNaN(value) && min != undefined && value < min)
				return this.formatMsg(MSG_VALID_TOO_SMALL, name, min);
			
			// 최대값 검사(숫자)
			if (!isNaN(value) && max != undefined && value > max)
				return this.formatMsg(MSG_VALID_TOO_LARGE, name, max);
			
			// 값 범위 검사(숫자)
			if (!isNaN(value) && range != undefined && (value < range[0] || value > range[1]))
				return this.formatMsg(MSG_VALID_OUT_OF_RANGE, name, range[0] + "~" + range[1]);
			
			// 최소길이 검사
			if (minLength != undefined && value.length < minLength)
				return this.formatMsg(MSG_VALID_TO_SHORT, name, minLength);
			
			// 최대길이 검사
			if (maxLength != undefined && value.length > maxLength)
				return this.formatMsg(MSG_VALID_TO_LONG, name, maxLength);
			
			// 길이 범위 검사
			if (lengthRange != undefined && (value.length < lengthRange[0] || value.length > lengthRange[1]))
				return this.formatMsg(MSG_VALID_OUT_OF_LENGTH_RANGE, name, lengthRange[0] + "~" + lengthRange[1]);
			
			// 값 일치여부 검사
			if (equals != undefined && value == equals)
				return this.formatMsg(MSG_VALID_NOT_EQUAL, name, equals);
			
			// 조건식 없을 경우 지정 regExp를 통해 검사
			if (regExp != undefined && !regExp.test(value))
				return this.formatMsg(msg ? msg : MSG_VALID_MISS_MATCH, name, regExp);
			
			// 모든 조건 만족시 메세지 반환 안함
			return undefined;
		},
		formatMsg: function(msg, name, condition)
		{
			if (!msg)
				return;
			
			return msg.replace("${name}", name ? name : "").replace("${condition}", condition);
		}
	};
	
	$.fn.validate = function(options)
	{
		var valid = true;
		
		this.each(function() // 하나의 조건으로 다회 검사가 일어날 수 있다.
		{
			currentTarget = this;
			try
			{
				// options 유효성 검사
				if (!options)
				{
					$.obAlertNotice(MSG_ERROR_VALIDATION_FAIL);
					valid = false;
					return false; // $.each break
				}
				else if (options.checked != undefined && options.checked == false) // checked가 false라면 그냥 유효한것으로 처리 
				{
					valid = true;
					return false; // $.each break
				}
			
				$(currentTarget).val(currentTarget.value.trim()); // 입력값 강제 트림
				var msg = validator.validation(currentTarget, options); // 유효성 검사 함수 실행
				
				if (msg) // 유효성 문제 발견시 처리 플로우	TODO: 콜백 함수 형태로 만들 것(for customizing callback)
				{
					$.obAlertNotice(msg);
					if (options.focus == undefined || options.focus == true)
						currentTarget.focus();
					
					valid = false;
					return false; // $.each break
				}
			}
			catch (e) // 오류 발생시 에러메세지 발생
			{
				console.log(e);
				$.obAlertNotice(MSG_ERROR_VALIDATION_FAIL);
				valid = false;
				return false; // $.each break
			}
		});

		return valid;
	};
	
	// 연속 검사
	$.validate = function(optionsList)
	{
		var valid = true;
		var options;
		var target;

		$(optionsList).each(function()
		{
			options = this;
			target = options.target;
			options.target = undefined;
			options.__proto__ = null;
			
			if (!target.validate(options)) // 조건중 문제 발생시 검사 중지
			{
				valid = false;
				return false; // $.each break
			}
		});
		
		return valid;
	};
})(window.jQuery);