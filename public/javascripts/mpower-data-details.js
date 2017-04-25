/**
 * Created by asha on 4/19/17.
 */
$(document).ready(function () {

    var formId = $("#formId").val();
    var dataId = $("#dataId").val();

    console.log(formId);

    if(formId==='1'){

        //console.log("1");

        $("#responseQuestion").html("গত মাসের পানি সংক্রান্ত সমস্যা সমাধান করা হয়েছিল কি?");

        var tbl = "" ;
        var thead =  "<table class='table table-striped table-bordered table-hover' id='userTable'>";
        var row="";


        $.ajax({

            type: "GET",
            url:  "/reports/getWaterDataDetails",
            data:{
                formId:formId,
                dataId:dataId
            },

            success: function(data) {

                console.log(data);

                var dataInfo = data.split(';');
                console.log(dataInfo.length);

                for (var i = 1; i < dataInfo.length-1; i = i + 13) {

                    var resType = dataInfo[i];
                    var waterSource = dataInfo[i + 1];
                    var numWaterSource = dataInfo[i + 2];
                    var activeWaterSource = dataInfo[i + 3];
                    var isPotable = dataInfo[i + 4];
                    var whyNotPotable = dataInfo[i + 5];
                    var whyNotPotableOther = dataInfo[i + 6];
                    var isTankCleaned = dataInfo[i + 7];
                    var isInformedAuthorityWaterProb = dataInfo[i + 8];
                    var howInformedWaterProb = dataInfo[i + 9];
                    var howInformedWaterProbOther = dataInfo[i + 10];
                    var waterProbSolvedAuthority = dataInfo[i + 11];
                    var rank = dataInfo[i + 12].substr(0, 1);

                    console.log("rank: " +rank);

                    row=row+'<tr class="demo">'+
                        '<td class="demo" width="60%"><strong> উত্তর দাতার ধরণ</strong></td>';



                    if(resType=='1'){

                        row= row+'<td>ছেলে</td>'+
                            '</tr>';
                    }

                    else if(resType=='2'){

                        row= row+'<td>মেয়ে</td>'+
                            '</tr>';
                    }

                    row=row+'<tr class="demo">'+
                        '<td class="demo" width="60%"><strong>খাবার পানির উৎস কি?</strong></td><td class="demo">';

                    var a = new Array();

                    a=waterSource.split(",");

                    console.log("a:" + a.length);

                    for(var j=0;j<a.length;j++) {

                        a[j]=a[j].trim();
                        console.log(a[j]);

                        if (a[j] == '1') {

                            row = row + 'টিউবওয়েল</br>';
                        }

                        else if (a[j] == '2') {

                            row = row + 'টেপ</br>';
                        }

                        else if (a[j] == '3') {

                            row = row + 'ফিল্টার</br>';
                        }

                        else if (a[j] == '4') {

                            row = row + 'কলসি</br>';
                        }

                    }

                        row = row + '</td></tr>';


                            row=row+'<tr class="demo">'+
                                '<td class="demo" width="60%"><strong>খাবার পানির কয়টি উৎস আছে ? </strong></td>'+
                                '<td class="demo">' + numWaterSource + '</td>'+
                                '</tr>';

                            row=row+'<tr class="demo">'+
                                '<td class="demo" width="60%"><strong>খাবার পানির কয়টি উৎস সচল আছে ? </strong></td>'+
                                '<td class="demo">' + activeWaterSource + '</td>'+
                                '</tr>';

                        row=row+'<tr class="demo">'+
                            '<td class="demo" width="60%"><strong>খাবার পানি পানের উপযোগী কিনা?</strong></td>';



                        if(isPotable=='1'){

                            row= row+'<td>হ্যাঁ</td>'+
                                '</tr>';
                        }

                        else if(isPotable=='0'){

                            row= row+'<td>না</td>'+
                                '</tr>';
                        }


                        row=row+'<tr class="demo">'+
                            '<td class="demo" width="60%"><strong>কেন উপযোগী নয়?</strong></td><td class="demo">';

                        var b = new Array();
                        b = whyNotPotable.split(",");
                        var flag = 0;

                        for(var k=0;k<b.length;k++){

                            b[k]=b[k].trim();

                            if(b[k]=='1'){

                                row= row+'দুর্গন্ধ</br>';
                            }

                            else if(b[k]=='2'){

                                row= row+'আয়রন</br>';
                            }

                            else if(b[k]=='3'){

                                row= row+'আর্সেনিক/ম্যাঙ্গানিজ</br>';
                            }

                            else if(b[k]=='4'){

                                row= row+'অন্যান্য</br>';
                                flag =1 ;


                            }

                        }

                        row = row + '</td></tr>';

                        if(flag==1){

                            waterOthers(whyNotPotableOther);
                        }


                        row=row+'<tr class="demo">'+
                            '<td class="demo" width="60%"><strong>গত একমাসে খাবার পানির ট্যাংকি পরিষ্কার করা হয়েছিল কি?</strong></td>';



                        if(isTankCleaned=='1'){

                            row= row+'<td class="demo">হ্যাঁ</td>';
                        }

                        else if(isTankCleaned=='2'){

                            row= row+'<td class="demo">না</td>';
                        }

                        else if(isTankCleaned=='3'){

                            row= row+'<td class="demo">প্রযোজ্য নয়</td>';
                        }

                        row = row +'</tr>';

                        row=row+'<tr class="demo">'+
                            '<td class="demo" width="60%"><strong>গত একমাসে স্কুলের খাবার পানি নিয়ে কোন সমস্যার কথা স্কুল কর্তৃপক্ষ কে জানানো হয়েছিল কিনা?</strong></td>';



                        if(isInformedAuthorityWaterProb=='1'){

                            row= row+'<td class="demo">হ্যাঁ</td>'+
                                '</tr>';

                            howInformedwater(howInformedWaterProb,waterProbSolvedAuthority);
                        }

                        else if(isInformedAuthorityWaterProb=='0'){

                            row= row+'<td class="demo">না</td>'+
                                '</tr>';
                        }


                    if(howInformedWaterProbOther!=""){

                        row=row+'<tr class="demo">'+
                            '<td class="demo" width="60%"><strong> অন্যান্য কিভাবে জানানো হয়েছিল? </strong></td>'+
                            '<td class="demo">' + howInformedWaterProbOther + '</td>'+
                            '</tr>';
                    }





                    row=row+'<tr class="demo">'+
                        '<td class="demo" width="60%"><strong>গত এক মাসের স্কুলের সার্বিক খাবার পানির ব্যবস্থা কেমন ছিল?</strong></td>';

                    if(rank=='1'){

                        row= row+'<td class="demo"> খুব খারাপ</td>'+
                            '</tr>';
                    }
                    else if(rank=='2'){

                        row= row+'<td class="demo">খারাপ</td>'+
                            '</tr>';
                    }

                    else if(rank=='3'){

                        row= row+'<td class="demo">মোটামোটি</td>'+
                            '</tr>';
                    }

                    else if(rank=='4'){

                        row= row+'<td class="demo">ভাল</td>'+
                            '</tr>';
                    }

                    else if(rank=='5'){

                        row= row+'<td class="demo">খুব ভাল</td>'+
                            '</tr>';
                    }



                }

                tbl =thead+ tbl+"<tbody>" + row + "</tbody>";
                $("#sample_2").html(tbl);

            }

        });

        function waterOthers(whyNotPotableOther) {

            row=row+'<tr class="demo">'+
                '<td class="demo" width="60%"><strong> অন্যান্য কি কারনে উপযোগী নয়? </strong></td>'+
                '<td class="demo">' + whyNotPotableOther + '</td>'+
                '</tr>';

        }


        function howInformedwater(howInformedWaterProb,waterProbSolvedAuthority) {

            row=row+'<tr class="demo">'+
                '<td class="demo" width="60%"><strong> যদি হ্যাঁ হয় তাহলে, কীভাবে জানানো হয়েছিলো?</strong></td>';



            if(howInformedWaterProb=='1'){

                row= row+'<td class="demo">স্কুল কতৃপক্ষ / SMC এর কাছে দরখাস্</td>'+
                    '</tr>';
            }

            else if(howInformedWaterProb=='2'){

                row= row+'<td class="demo">প্রধান শিক্ষক/ উপ প্রধান শিক্ষক কাছে মৌখিকভাবে</td>'+
                    '</tr>';
            }

            else if(howInformedWaterProb=='3'){

                row= row+'<td class="demo">শ্রেণী শিক্ষকের কাছে মৌখিকভাবে</td>'+
                    '</tr>';
            }

            else if(howInformedWaterProb=='4'){

                row= row+'<td class="demo">নস্কুল ম্যানেজিং কমিটির সদস্যের কাছে মৌখিকভাবো</td>'+
                    '</tr>';
            }

            else if(howInformedWaterProb=='5'){

                row= row+'<td class="demo">অন্যান্য</td>'+
                    '</tr>';
            }

            row=row+'<tr class="demo">'+
                '<td class="demo" width="60%"><strong>কর্তৃপক্ষ কত দিনের মধ্যে ব্যবস্থা নিয়েছিল?</strong></td>';

            if(waterProbSolvedAuthority=='1'){

                row= row+'<td class="demo"> সাথে সাথে /এক দিনের মধ্যে</td>'+
                    '</tr>';
            }
            else if(waterProbSolvedAuthority=='2'){

                row= row+'<td class="demo">২-৩ দিনের মধ্যে</td>'+
                    '</tr>';
            }

            else if(waterProbSolvedAuthority=='3'){

                row= row+'<td class="demo">৪-৭ দিনের মধ্যে</td>'+
                    '</tr>';
            }

            else if(waterProbSolvedAuthority=='4'){

                row= row+'<td class="demo">৮-৩০ দিনের মধ্যে</td>'+
                    '</tr>';
            }

            else if(waterProbSolvedAuthority=='5'){

                row= row+'<td class="demo">৩০ দিনের থেকে বেশি সময়ে</td>'+
                    '</tr>';
            }

            else if(waterProbSolvedAuthority=='6'){

                row= row+'<td class="demo">এখনও কোন ব্যবস্থা নেন নি</td>'+
                    '</tr>';
            }

            else if(waterProbSolvedAuthority=='7'){

                row= row+'<td class="demo">জানা নেই</td>'+
                    '</tr>';
            }


        }


    }

    else if(formId==='2'){

        $("#responseQuestion").html("গত মাসের পয়ঃনিষ্কাশন সংক্রান্ত সমস্যা সমাধান করা হয়েছিল কি?");

        var tbl = "" ;
        var thead =  "<table class='table table-striped table-bordered table-hover' id='userTable'>";
        var row="";

        console.log("2");

        $.ajax({

            type: "GET",
            url:  "/reports/getSanitationDataDetails",
            data:{
                formId:formId,
                dataId:dataId
            },

            success: function(data) {
                var json = JSON.parse(data);
                console.log(json);

                var dataInfo = data.split(';');

                for (var i = 1; i < dataInfo.length-1; i = i + 10) {

                    var resType=dataInfo[i];
                    var numberOfToiletUnusable=dataInfo[i+1];
                    var whyToiletUnusable=dataInfo[i+2];
                    var toiletCleanInterval=dataInfo[i+3];
                    var harpic=dataInfo[i+4];
                    var isSanitationProbInformed=dataInfo[i+5];
                    var howInformedProb=dataInfo[i+6];
                    var howInformedProbOther=dataInfo[i+7];
                    var tookStep=dataInfo[i+8];
                    var rank=dataInfo[i+9].substr(0,1);
                    console.log(rank);

                row=row+'<tr class="demo">'+
                    '<td class="demo" width="60%"><strong> উত্তর দাতার ধরণ</strong></td>';



                if(resType=='1'){

                row= row+'<td class="demo">ছেলে</td>'+
                    '</tr>';
                }

                else if(resType=='2'){

                    row= row+'<td class="demo" >মেয়ে</td>'+
                        '</tr>';
                }

                row=row+'<tr class="demo">'+
                    '<td class="demo" width="60%"><strong>স্কুলে কয়টি টয়লেট ব্যবহারের অনুপযোগী? (বালক/বালিকা)</strong></td>'+
                    '<td class="demo">'+numberOfToiletUnusable+'</td>'+
                    '</tr>';

                    row=row+'<tr class="demo">'+
                        '<td class="demo" width="60%"><strong>কি কারনে অনুপযোগী?</strong></td><td class="demo">';

                    var a = new Array();

                    a=whyToiletUnusable.split(",");

                    for(var j=1;j<a.length;j++){

                        if(a[j]=='1'){

                            row= row+'টয়লেটের পানির কল সচল নেই</br>';
                        }

                        else if(a[j]=='2'){

                            row= row+'টয়লেটের পানির উৎস বা পানির কল নাগালের মধ্যে নেই</br>';
                        }

                        else if(a[j]=='3'){

                            row= row+'টয়লেটে পানি নেই</br>';
                        }

                        else if(a[j]=='4'){

                            row= row+'টয়লেটের ছিটকিনি/লক কাজ করে না</br>';
                        }

                        else if(a[j]=='5'){

                            row= row+'টয়লেটের দরজা ভাঙ্গা/দরজায় ছিদ্র</br>';
                        }

                        else if(a[j]=='6'){

                            row= row+'টয়লেটে মগ/বদনা/বালতি নেই</br>';
                        }

                        else if(a[j]=='7'){

                            row= row+'কমোডে/প্যানে ময়লা জমে থাকা/পানি উপচে পড়ছে</br>';
                        }

                        else if(a[j]=='8'){

                            row= row+'টয়লেটে পানি জমে আছে</br>';
                        }

                        else if(a[j]=='9'){

                            row= row+'মেয়েদের টয়লেটে ন্যপকিন/প্যাড ফেলার আলাদা ঝুড়ি নেই</br>';
                        }

                        else if(a[j]=='10'){

                            row= row+'টয়লেটে পর্যাপ্ত  আলো-বাতাসে/ বাল্ব এর ব্যবস্থা নেই</br>';
                        }

                        else if(a[j]=='11'){

                            row= row+'পোকা মাকড়ের উপদ্রব</br>';
                        }

                        else if(a[j]=='12'){

                            row= row+'দুর্গন্ধ</br>';
                        }

                        else if(a[j]=='13'){

                            row= row+'অন্যান্য</br>';
                        }

                    }

                    row = row + '</td></tr>';

                    row=row+'<tr class="demo">'+
                        '<td class="demo" width="60%"><strong>গত একমাসে স্কুলের টয়লেট কি রকম বিরতিতে পরিষ্কার করা হয়েছে?</strong></td>';
                    if(toiletCleanInterval=='1'){

                        row= row+'<td class="demo">গত মাসে পরিষ্কার করা হয়নি</td>'+
                            '</tr>';
                    }

                    else if(toiletCleanInterval=='2'){

                        row= row+'<td class="demo">মাসে একবার বা তার বেশিবার পরিষ্কার করা হয়েছে</td>'+
                            '</tr>';
                    }

                    else if(toiletCleanInterval=='3'){

                        row= row+'<td class="demo">মাসে একবার বা তার বেশিবার পরিষ্কার করা হয়েছে</td>'+
                            '</tr>';
                    }

                    else if(toiletCleanInterval=='4'){

                        row= row+'<td class="demo">সপ্তাহে একবার বা তার বেশি বার পরিষ্কার করা হয়েছে</td>'+
                            '</tr>';
                    }

                    else if(toiletCleanInterval=='5'){

                        row= row+'<td class="demo">প্রতিদিন পরিষ্কার করা হয়েছে</td>'+
                            '</tr>';
                    }

                    row=row+'<tr class="demo">'+
                        '<td class="demo" width="60%"><strong>টয়লেট পরিষ্কার করার সময় জীবাণুনাশক উপকরণ (যেমনঃ হারপিক, ফিনাইল) ব্যবহার করাঃ</strong></td>';
                    if(harpic=='1'){

                        row= row+'<td class="demo">কোনো টয়লেটে ব্যবহার করা হয়েছে</td>'+
                            '</tr>';
                    }

                    else if(harpic=='2'){

                        row= row+'<td class="demo">অর্ধেক সংখ্যক টয়লেটে ব্যবহার করা হয়েছে</td>'+
                            '</tr>';
                    }

                    else if(harpic=='3'){

                        row= row+'<td class="demo">অধিকাংশ টয়লেটে ব্যবহার করা হয়েছে</td>'+
                            '</tr>';
                    }

                    else if(harpic=='4'){

                        row= row+'<td class="demo">সপ্তাহে একবার বা তার বেশি বার পরিষ্কার করা হয়েছে</td>'+
                            '</tr>';
                    }

                    else if(harpic=='5'){

                        row= row+'<td class="demo">সব  টয়লেটে ব্যবহার করা হয়েছে</td>'+
                            '</tr>';
                    }

                    row=row+'<tr class="demo">'+
                        '<td width="60%"><strong>গত একমাসে স্কুলের পয়ঃনিষ্কাশন নিয়ে কোন সমস্যার কথা স্কুল কর্তৃপক্ষ কে জানানো হয়েছিল কিনা?</strong></td>';



                    if(isSanitationProbInformed=='1'){

                        row= row+'<td class="demo">হ্যাঁ</td>'+
                            '</tr>';

                        yesSanitationFunction(howInformedProb,tookStep);
                    }

                    else if(isSanitationProbInformed=='0'){

                        row= row+'<td class="demo">না</td>'+
                            '</tr>';
                    }


                    if(howInformedProbOther!=""){

                        row=row+'<tr class="demo">'+
                            '<td class="demo" width="60%"><strong>অন্যান্য কিভাবে জানানো হয়েছিল?</strong></td>'+
                            '<td class="demo">' + howInformedProbOther +'</td>'+
                            '</tr>';


                    }


                    row=row+'<tr class="demo">'+
                        '<td class="demo" width="60%"><strong> গত এক মাসের স্কুলের সার্বিক পয়ঃনিষ্কাশন ব্যবস্থা কেমন ছিল?</strong></td>';

                    if(rank=='1'){

                        row= row+'<td class="demo">খুব খারাপ</td>'+
                            '</tr>';
                    }

                    else if(rank=='2'){

                        row= row+'<td class="demo">খারাপ</td>'+
                            '</tr>';
                    }

                    else if(rank=='3'){

                        row= row+'<td class="demo">মোটামোটি</td>'+
                            '</tr>';
                    }

                    else if(rank=='4'){

                        row= row+'<td class="demo">ভাল</td>'+
                            '</tr>';
                    }

                    else if(rank=='5'){

                        row= row+'<td class="demo">খুব ভাল</td>'+
                            '</tr>';
                    }





                }

                tbl =thead+ tbl+"<tbody>" + row + "</tbody>";
                $("#sample_2").html(tbl);

                 function yesSanitationFunction(howInformedProb,tookStep){

                     row=row+'<tr class="demo">'+
                         '<td class="demo" width="60%"><strong> যদি হ্যাঁ হয় তাহলে, কীভাবে জানানো হয়েছিলো?</strong></td>';

                     if(howInformedProb=='1'){

                         row= row+'<td class="demo">স্কুল কতৃপক্ষ / SMC এর কাছে দরখাস্ত</td>'+
                             '</tr>';
                     }

                     else if(howInformedProb=='2'){

                         row= row+'<td class="demo">প্রধান শিক্ষক/ উপ প্রধান শিক্ষক কাছে মৌখিকভাবে</td>'+
                             '</tr>';
                     }

                     else if(howInformedProb=='3'){

                         row= row+'<td class="demo">শ্রেণী শিক্ষকের কাছে মৌখিকভাবে</td>'+
                             '</tr>';
                     }

                     else if(howInformedProb=='4'){

                         row= row+'<td class="demo">স্কুল ম্যানেজিং কমিটির সদস্যের কাছে মৌখিকভাবে</td>'+
                             '</tr>';
                     }

                     else if(howInformedProb=='5'){

                         row= row+'<td class="demo">অন্যান্য</td>'+
                             '</tr>';
                     }

                     row=row+'<tr class="demo">'+
                         '<td class="demo" width="60%"><strong>কর্তৃপক্ষ কত দিনের মধ্যে ব্যবস্থা নিয়েছিল?</strong></td>';

                     if(tookStep=='1'){

                         row= row+'<td class="demo">সাথে সাথে /এক দিনের মধ্যে</td>'+
                             '</tr>';
                     }

                     else if(tookStep=='2'){

                         row= row+'<td class="demo">২-৩ দিনের মধ্যে</td>'+
                             '</tr>';
                     }

                     else if(tookStep=='3'){

                         row= row+'<td class="demo">৪-৭ দিনের মধ্যে</td>'+
                             '</tr>';
                     }

                     else if(tookStep=='4'){

                         row= row+'<td class="demo">৮-৩০ দিনের মধ্যে</td>'+
                             '</tr>';
                     }

                     else if(tookStep=='5'){

                         row= row+'<td class="demo">৩০ দিনের থেকে বেশি সময়ে</td>'+
                             '</tr>';
                     }

                     else if(tookStep=='6'){

                         row= row+'<td class="demo">সএখনও কোন ব্যবস্থা নেন নি</td>'+
                             '</tr>';
                     }

                     else if(tookStep=='7'){

                         row= row+'<td class="demo">জানা নেই</td>'+
                             '</tr>';
                     }


                 }

            }
        });


    }

    else if(formId==='3'){

        $("#responseQuestion").html("গত মাসের স্কুলের পরিবেশ সংক্রান্ত সমস্যা সমাধান করা হয়েছিল কি?");

        var tbl = "" ;
        var thead =  "<table class='table table-striped table-bordered table-hover' id='userTable'>";
        var row="";


        //console.log("3");

            $.ajax({

                type: "GET",
                url: "/reports/getEnviornmentDataDetails",
                data: {
                    formId: formId,
                    dataId:dataId
                },

                success: function (data) {

                    console.log(data);

                    var dataInfo = data.split(";");

                    console.log(dataInfo.length);

                    for( var i=1;i<dataInfo.length-1; i=i+15){

                        var resType=dataInfo[i];
                        var cleanIntervalSchoolYard=dataInfo[i+1];
                        var cleanIntervalClassRoom=dataInfo[i+2];
                        var stuHearTeacher=dataInfo[i+3];
                        var stuSeatArrange=dataInfo[i+4];
                        var teacherStage=dataInfo[i+5];
                        var complainedSchoolEnvironment=dataInfo[i+6];
                        var howInformedEnvironmentProb=dataInfo[i+7];
                        var howInformedEnvironmentProbOther=dataInfo[i+8];
                        var tookStepEnvironmentProb=dataInfo[i+9];
                        var rankEnvironment=dataInfo[i+10];
                        var rankEduQuality=dataInfo[i+11];
                        var scareSafeSchoolWay=dataInfo[i+12];
                        var yhyNotFeelSage=dataInfo[i+13];
                        var schoolCanteen=dataInfo[i+14].substr(0,1);

                        row=row+'<tr class="demo">'+
                            '<td class="demo" width="60%"><strong> উত্তর দাতার ধরণ</strong></td>';

                        if(resType=='1'){

                            row= row+'<td class="demo">ছেলে</td>'+
                                '</tr>';
                        }

                        else if(resType=='2'){

                            row= row+'<td class="demo">মেয়ে</td>'+
                                '</tr>';
                        }

                        row=row+'<tr class="demo">'+
                            '<td class="demo"width="60%"><strong>গত একমাসে স্কুলের আঙ্গিনা কি রকম বিরতিতে পরিষ্কার করা হয়েছে?</strong></td>';

                        if(cleanIntervalSchoolYard=='1'){

                            row= row+'<td class="demo">গত মাসে পরিষ্কার করা হয়নি</td>'+
                                '</tr>';
                        }

                        else if(cleanIntervalSchoolYard=='2'){

                            row= row+'<td class="demo">মাসে একবার বা তার বেশিবার পরিষ্কার করা হয়েছে</td>'+
                                '</tr>';
                        }

                        else if(cleanIntervalSchoolYard=='3'){

                            row= row+'<td class="demo">মাসে একবার বা তার বেশিবার পরিষ্কার করা হয়েছে</td>'+
                                '</tr>';
                        }
                        else if(cleanIntervalSchoolYard=='4'){

                            row= row+'<td class="demo">সপ্তাহে একবার বা তার বেশি বার পরিষ্কার করা হয়েছে</td>'+
                                '</tr>';
                        }
                        else if(cleanIntervalSchoolYard=='5'){

                            row= row+'<td class="demo">্রতিদিন পরিষ্কার করা হয়েছে</td>'+
                                '</tr>';
                        }

                        row=row+'<tr class="demo">'+
                            '<td class="demo" width="60%"><strong> গত একমাসে স্কুলের ক্লাসরুম কি রকম বিরতিতে পরিষ্কার করা হয়েছে?</strong></td>';

                        if(cleanIntervalClassRoom=='1'){

                            row= row+'<td class="demo">গত মাসে পরিষ্কার করা হয়নি</td>'+
                                '</tr>';
                        }

                        else if(cleanIntervalClassRoom=='2'){

                            row= row+'<td class="demo">মাসে একবার বা তার বেশিবার পরিষ্কার করা হয়েছে</td>'+
                                '</tr>';
                        }

                        else if(cleanIntervalClassRoom=='3'){

                            row= row+'<td class="demo">মাসে একবার বা তার বেশিবার পরিষ্কার করা হয়েছে</td>'+
                                '</tr>';
                        }
                        else if(cleanIntervalClassRoom=='4'){

                            row= row+'<td class="demo">সপ্তাহে একবার বা তার বেশি বার পরিষ্কার করা হয়েছে</td>'+
                                '</tr>';
                        }
                        else if(cleanIntervalClassRoom=='5'){

                            row= row+'<td class="demo">্রতিদিন পরিষ্কার করা হয়েছে</td>'+
                                '</tr>';
                        }

                        row=row+'<tr class="demo">'+
                            '<td class="demo" width="60%"><strong> ক্লাস রুমের সকলে শিক্ষকের বক্তব্য পরিষ্কারভাবে শুনতে পায় কি না?</strong></td>';

                        //console.log(stuHearTeacher);

                        if(stuHearTeacher=='1'){

                            row= row+'<td class="demo">একেবারেই পরিষ্কারভাবে শুনতে পায় না</td>'+
                                '</tr>';
                        }

                        else if(stuHearTeacher=='2'){

                            row= row+'<td class="demo">মাঝে মাঝে পরিষ্কারভাবে শুনতে পায়</td>'+
                                '</tr>';
                        }

                        else if(stuHearTeacher=='3'){

                            row= row+'<td class="demo">পরিষ্কারভাবে শুনতে পায়</td>'+
                                '</tr>';
                        }

                        row=row+'<tr class="demo">'+
                            '<td class="demo" width="60%"><strong> ক্লাস রুমে বসার জন্য পর্যাপ্ত ব্যবস্থা আছে কি না? (বেঞ্চ,টেবিল ইত্যাদি)</strong></td>';

                        //console.log(stuSeatArrange);

                        if(stuSeatArrange=='1'){

                            row= row+'<td class="demo">হ্যাঁ</td>'+
                                '</tr>';
                        }

                        else if(stuSeatArrange=='2'){

                            row= row+'<td class="demo">না</td>'+
                                '</tr>';
                        }

                        row=row+'<tr class="demo">'+
                            '<td class="demo" width="60%"><strong> ক্লাস রুমে শিক্ষকদের জন্য স্টেজ/ডায়াস আছে ?</strong></td>';

                        //console.log(stuSeatArrange);

                        if(teacherStage=='1'){

                            row= row+'<td class="demo">হ্যাঁ</td>'+
                                '</tr>';
                        }

                        else if(teacherStage=='2'){

                            row= row+'<td class="demo">না</td>'+
                                '</tr>';
                        }

                        row=row+'<tr class="demo">'+
                            '<td class="demo" width="60%"><strong> গত একমাসে স্কুলের পরিবেশ নিয়ে  কোন সমস্যার কথা স্কুল কর্তৃপক্ষ কে জানানো হয়েছিল কিনা?</strong></td>';

                        //console.log(stuSeatArrange);

                        if(complainedSchoolEnvironment=='1'){

                            row= row+'<td class="demo">হ্যাঁ</td>'+
                                '</tr>';
                            yesEnvironmentFunction(howInformedEnvironmentProb,tookStepEnvironmentProb);
                        }

                        else if(complainedSchoolEnvironment=='2'){

                            row= row+'<td class="demo">না</td>'+
                                '</tr>';


                        }




                        row=row+'<tr class="demo">'+
                            '<td class="demo" width="60%"><strong> গত এক মাসের স্কুলের সার্বিক পরিবেশ কেমন ছিল?</strong></td>';

                        if(rankEnvironment=='1'){

                            row= row+'<td class="demo">খুব খারাপ</td>'+
                                '</tr>';
                        }

                        else if(rankEnvironment=='2'){

                            row= row+'<td class="demo">খারাপ</td>'+
                                '</tr>';
                        }

                        else if(rankEnvironment=='3'){

                            row= row+'<td class="demo">মোটামোটি</td>'+
                                '</tr>';
                        }

                        else if(rankEnvironment=='4'){

                            row= row+'<td class="demo">ভাল</td>'+
                                '</tr>';
                        }

                        else if(rankEnvironment=='5'){

                            row= row+'<td class="demo">খুব ভাল</td>'+
                                '</tr>';
                        }

                        row=row+'<tr>'+
                            '<td width="60%"><strong> গত এক মাসের স্কুলের সার্বিক শিক্ষার মান কেমন ছিল?</strong></td>';

                        if(rankEduQuality=='1'){

                            row= row+'<td class="demo">খুব খারাপ</td>'+
                                '</tr>';
                        }

                        else if(rankEduQuality=='2'){

                            row= row+'<td class="demo">খারাপ</td>'+
                                '</tr>';
                        }

                        else if(rankEduQuality=='3'){

                            row= row+'<td class="demo">মোটামোটি</td>'+
                                '</tr>';
                        }

                        else if(rankEduQuality=='4'){

                            row= row+'<td class="demo">ভাল</td>'+
                                '</tr>';
                        }

                        else if(rankEduQuality=='5'){

                            row= row+'<td class="demo">খুব ভাল</td>'+
                                '</tr>';
                        }

                        row=row+'<tr class="demo">'+
                            '<td class="demo" width="60%"><strong> স্কুলে যাওয়া আসার-পথে বা স্কুলে শিক্ষার্থীরা নিজেদের নিরাপদ ভাবে কি ?</strong></td>';

                        //console.log(stuSeatArrange);

                        if(scareSafeSchoolWay=='1'){

                            row= row+'<td class="demo">হ্যাঁ</td>'+
                                '</tr>';
                        }

                        else if(scareSafeSchoolWay=='2'){

                            row= row+'<td class="demo">না</td>'+
                                '</tr>';

                            noEnviormentFunction(yhyNotFeelSage);
                        }



                        row=row+'<tr class="demo">'+
                            '<td class="demo" width="60%"><strong> গত এক মাসে স্কুলে  খাবার ক্যান্টিন ও খবারের মান</strong></td>';

                        if(schoolCanteen=='1'){

                            row= row+'<td class="demo">কোন কেন্টিন নাই</td>'+
                                '</tr>';
                        }

                        else if(schoolCanteen=='2'){

                            row= row+'<td class="demo">ক্যান্টিন বন্ধ হয়ে গেছে</td>'+
                                '</tr>';
                        }

                        else if(schoolCanteen=='3'){

                            row= row+'<td class="demo">খাবারের মান খারাপ হয়েছে</td>'+
                                '</tr>';
                        }

                        else if(schoolCanteen=='4'){

                            row= row+'<td class="demo">খাবারের মান আগের মতই রয়েছে</td>'+
                                '</tr>';
                        }

                        else if(schoolCanteen=='5'){

                            row= row+'<td class="demo">খাবারের মান ভাল হয়েছে</td>'+
                                '</tr>';
                        }




                    }

                    tbl =thead+ tbl+"<tbody>" + row + "</tbody>";
                    $("#sample_2").html(tbl);

                    function yesEnvironmentFunction(howInformedEnvironmentProb,tookStepEnvironmentProb){

                        row=row+'<tr class="demo">'+
                            '<td class="demo" width="60%"><strong> যদি হ্যাঁ হয় তাহলে, কীভাবে জানানো হয়েছিলো?</strong></td>';

                        if(howInformedEnvironmentProb=='1'){

                            row= row+'<td class="demo">স্কুল কতৃপক্ষ / SMC এর কাছে দরখাস্ত</td>'+
                                '</tr>';
                        }

                        else if(howInformedEnvironmentProb=='2'){

                            row= row+'<td class="demo">প্রধান শিক্ষক/ উপ প্রধান শিক্ষক কাছে মৌখিকভাবে</td>'+
                                '</tr>';
                        }

                        else if(howInformedEnvironmentProb=='3'){

                            row= row+'<td class="demo">শ্রেণী শিক্ষকের কাছে মৌখিকভাবে</td>'+
                                '</tr>';
                        }

                        else if(howInformedEnvironmentProb=='4'){

                            row= row+'<td class="demo">স্কুল ম্যানেজিং কমিটির সদস্যের কাছে মৌখিকভাবে</td>'+
                                '</tr>';
                        }

                        else if(howInformedEnvironmentProb=='5'){

                            row= row+'<td class="demo">অন্যান্য</td>'+
                                '</tr>';

                            row=row+'<tr class="demo">'+
                                '<td class="demo" width="60%"><strong>অন্যান্য কীভাবে জানানো হয়েছিলো?</strong></td>'+
                                '<td class="demo">' + howInformedEnvironmentProbOther +'</td>'+
                                '</tr>';
                        }

                        row=row+'<tr class="demo">'+
                            '<td class="demo" width="60%"><strong>কর্তৃপক্ষ কত দিনের মধ্যে ব্যবস্থা নিয়েছিল?</strong></td>';

                        if(tookStepEnvironmentProb=='1'){

                            row= row+'<td class="demo">সাথে সাথে /এক দিনের মধ্যে</td>'+
                                '</tr>';
                        }

                        else if(tookStepEnvironmentProb=='2'){

                            row= row+'<td class="demo">২-৩ দিনের মধ্যে</td>'+
                                '</tr>';
                        }

                        else if(tookStepEnvironmentProb=='3'){

                            row= row+'<td class="demo">৪-৭ দিনের মধ্যে</td>'+
                                '</tr>';
                        }

                        else if(tookStepEnvironmentProb=='4'){

                            row= row+'<td class="demo">৮-৩০ দিনের মধ্যে</td>'+
                                '</tr>';
                        }

                        else if(tookStepEnvironmentProb=='5'){

                            row= row+'<td class="demo">৩০ দিনের থেকে বেশি সময়ে</td>'+
                                '</tr>';
                        }

                        else if(tookStepEnvironmentProb=='6'){

                            row= row+'<td class="demo">সএখনও কোন ব্যবস্থা নেন নি</td>'+
                                '</tr>';
                        }

                        else if(tookStepEnvironmentProb=='7'){

                            row= row+'<td class="demo">জানা নেই</td>'+
                                '</tr>';
                        }

                    }

                    function noEnviornmentFunction(yhyNotFeelSage){


                        row=row+'<tr class="demo">'+
                            '<td class="demo" width="60%"><strong>যদি না করে কেন করে না ?</strong></td><td class="demo">';

                        var a = new Array();

                        a=yhyNotFeelSage.split(",");

                        for(var j=1;j<a.length;j++){

                            if(a[j]=='1'){

                                row= row+'যৌন হয়রানি জনিত আশংকা</br>';
                            }

                            else if(a[j]=='2'){

                                row= row+'সড়ক দূর্ঘটনা</br>';
                            }

                            else if(a[j]=='3'){

                                row= row+'্কুল খুব দূরে অবস্থিত</br>';
                            }

                            else if(a[j]=='4'){

                                row= row+'স্কুলে শারীরিক শাস্তির ভয়</br>';
                            }

                            else if(a[j]=='5'){

                                row= row+'বহিরাগত / অন্য কারো দ্বারা আক্রান্তের শঙ্কা</br>';
                            }

                            else if(a[j]=='6'){

                                row= row+'অন্যান্য</br>';
                            }



                        }

                        row = row + '</td></tr>';
                    }

                }

            });

    }

    else if(formId==='4') {

        $("#responseQuestion").html("গত মাসের স্কুলের খেলা ও বিনোদন সংক্রান্ত সমস্যা সমাধান করা হয়েছিল কি?");


        //console.log("4");
        var tbl = "" ;
        var thead =  "<table class='table table-striped table-bordered table-hover' id='userTable'>";
        var row="";

        $.ajax({

            type: "GET",
            url: "/reports/getSportsDataDetails",
            data: {
                formId: formId,
                dataId:dataId
            },

            success: function (data) {

                console.log(data);

                var dataInfo = data.split(";");

                console.log(dataInfo.length);

                for( var i=1;i<dataInfo.length-1; i=i+9){

                    var resType = dataInfo[i];
                    var facilitiesAvailable = dataInfo[i+1];
                    var instrumentUsable = dataInfo[i+2];
                    var schoolType = dataInfo[i+3];
                    var instrumentEqualAccess = dataInfo[i+4];
                    var whyNotEqualAccess = dataInfo[i+5];
                    var sportsTeacher = dataInfo[i+6];
                    var lastMonthActivity = dataInfo[i+7];
                    var rankSportsRecreation = dataInfo[i+8].substr(0,1);

                    row=row+'<tr class="demo">'+
                        '<td class="demo" width="60%"><strong> উত্তর দাতার ধরণ</strong></td>';

                    if(resType=='1'){

                        row= row+'<td class="demo">ছেলে</td>'+
                            '</tr>';
                    }

                    else if(resType=='2'){

                        row= row+'<td>মেয়ে</td>'+
                            '</tr>';
                    }

                    row=row+'<tr class="demo">'+
                        '<td class="demo" width="60%"><strong> শারীরিক ও মানসিক বিকাশের জন্য কি কি ব্যবস্থা আছে ?</strong></td><td class="demo">';

                    var a= new Array();

                    a=facilitiesAvailable.split(",");

                    for (var j=1;j<a.length;j++){

                        if(a[j]=='1'){

                            row = row + 'খেলার মাঠ</br>';
                        }

                        else  if(a[j]=='2'){

                            row = row + 'খেলার উপকরণ</br>';
                        }

                        else  if(a[j]=='3'){

                            row = row + 'বিনোদন উপকরণ (যেমনঃ হারমোনিয়াম,তবলা ইত্যাদি)</br>';
                        }

                        else  if(a[j]=='4'){

                            row = row + 'পাঠাগার</br>';
                        }

                        else  if(a[j]=='5'){

                            row = row + 'বিভিন্ন প্রতিযোগিতা (বিতর্ক, আবৃত্তি, নাটক ইত্যাদি)</br>';
                        }

                        else  if(a[j]=='6'){

                            row = row + 'স্কাউট, গার্লস গাইড, বয়েস গাইড</br>';
                        }

                        else  if(a[j]=='7'){

                            row = row + 'কোন উপকরণ নেই</br>';
                        }


                    }

                    row = row + '</td></tr>';

                    row=row+'<tr class="demo">'+
                        '<td class="demo" width="60%"><strong> খেলার ও বিনোদন উপকরণ কি ব্যবহার উপযোগী?</strong></td>';

                    if(instrumentUsable=='1'){

                        row= row+'<td>হ্যাঁ</td>'+
                            '</tr>';
                    }

                    else if(instrumentUsable=='0'){

                        row= row+'<td>না</td>'+
                            '</tr>';
                    }

                    row=row+'<tr class="demo">'+
                        '<td class="demo" width="60%"><strong> বিদ্যালয়ের ধরণ?</strong></td>';

                    if(schoolType=='1'){

                        row= row+'<td class="demo">বালিকা বিদ্যালয়</td>'+
                            '</tr>';
                    }

                    else if(schoolType=='2'){

                        row= row+'<td class="demo">বালক বিদ্যালয়</td>'+
                            '</tr>';
                    }

                    else if(schoolType=='3'){

                        row= row+'<td class="demo">যৌথ বিদ্যালয়</td>'+
                            '</tr>';
                    }

                    row=row+'<tr>'+
                        '<td class="demo" width="60%"><strong>  ছেলে মেয়ে উভয়ে এই সকল বিনোদন উপকরণ সমানভাবে ব্যবহার করে কি ?</strong></td>';

                    if(instrumentEqualAccess=='1'){

                        row= row+'<td class="demo">হ্যাঁ</td>'+
                            '</tr>';
                    }

                    else if(instrumentEqualAccess=='0'){

                        row= row+'<td class="demo">না</td>'+
                            '</tr>';

                        noSportsFunction(whyNotEqualAccess);
                    }



                    row=row+'<tr class="demo">'+
                        '<td class="demo" width="60%"><strong>স্কুলে কি কোন ক্রীড়া শিক্ষক আছে ?</strong></td>';

                    if(sportsTeacher=='1'){

                        row= row+'<td class="demo">হ্যাঁ</td>'+
                            '</tr>';
                    }

                    else if(sportsTeacher=='0'){

                        row= row+'<td class="demo">না</td>'+
                            '</tr>';
                    }

                    row=row+'<tr class="demo">'+
                        '<td class="demo" width="60%"><strong> গত এক মাসে  নিম্নলিখিত কোন কার্যক্রম অনুষ্ঠিত হয়েছে ?</strong></td><td class="demo">';

                    var c= new Array();

                    c=lastMonthActivity.split(",");

                    for (var m=1;m<c.length;m++){

                        if(c[m]=='1'){

                            row = row + 'আন্তঃস্কুল ক্রীড়া প্রতিযোগিতা</br>';
                        }

                        else  if(c[m]=='2'){

                            row = row + 'আন্তঃস্কুল বিতর্ক প্রতিযোগিতা</br>';
                        }

                        else  if(c[m]=='3'){

                            row = row + 'আন্তঃক্লাস ক্রীড়া প্রতিযোগিতা</br>';
                        }

                        else  if(c[m]=='4'){

                            row = row + 'আন্তঃক্লাস বিতর্ক প্রতিযোগিতা</br>';
                        }

                        else  if(c[m]=='5'){

                            row = row + 'সাংস্কৃতিক উৎসব</br>';
                        }

                        else  if(c[m]=='6'){

                            row = row + 'শিক্ষা সফর</br>';
                        }

                        else  if(c[m]=='7'){

                            row = row + 'বার্ষিক ক্রীড়া প্রতিযোগিতা</br>';
                        }

                        else  if(c[m]=='8'){

                            row = row + 'অন্যান্য</br>';
                        }


                    }

                    row = row + '</td></tr>';

                    row=row+'<tr class="demo">'+
                        '<td class="demo" width="60%"><strong> গত এক মাসের স্কুলের সার্বিক ক্রীড়া ও বিনোদন ব্যবস্থা কেমন ছিল?</strong></td>';

                    if(rankSportsRecreation=='1'){

                        row= row+'<td class="demo">খুব খারাপ</td>'+
                            '</tr>';
                    }

                    else if(rankSportsRecreation=='2'){

                        row= row+'<td class="demo">খারাপ</td>'+
                            '</tr>';
                    }

                    else if(rankSportsRecreation=='3'){

                        row= row+'<td class="demo">মোটামোটি</td>'+
                            '</tr>';
                    }

                    else if(rankSportsRecreation=='4'){

                        row= row+'<td class="demo">ভাল</td>'+
                            '</tr>';
                    }

                    else if(rankSportsRecreation=='5'){

                        row= row+'<td class="demo">খুব ভাল</td>'+
                            '</tr>';
                    }


                }

                tbl =thead+ tbl+"<tbody>" + row + "</tbody>";
                $("#sample_2").html(tbl);

                function noSportsFunction(whyNotEqualAccess){


                    row=row+'<tr class="demo">'+
                        '<td class="demo" width="60%"><strong> যদি না করে কেন করে না ?</strong></td><td class="demo">';

                    var b= new Array();

                    b=whyNotEqualAccess.split(",");

                    for (var k=1;k<b.length;k++){

                        if(b[k]=='1'){

                            row = row + 'এক সাথে খেলার যথেষ্ট জায়গা নেই</br>';
                        }

                        else  if(b[k]=='2'){

                            row = row + 'ছেলে মেয়ে একসাথে / পাশাপাশি খেলতে চায় না</br>';
                        }

                        else  if(b[k]=='3'){

                            row = row + 'দায়িত্বপ্রাপ্ত শিক্ষক/ শিক্ষিকার অনাগ্রহ</br>';
                        }

                        else  if(b[k]=='4'){

                            row = row + 'অভিভাবকের অনাগ্রহ</br>';
                        }

                        else  if(b[k]=='5'){

                            row = row + 'যথেষ্ট সময়ের অভাব</br>';
                        }

                        else  if(b[k]=='6'){

                            row = row + 'কমন রুম নেই</br>';
                        }

                        else  if(b[k]=='7'){

                            row = row + 'মেয়েদের জন্য আলাদা উপকরণ নেই</br>';
                        }

                        else  if(b[k]=='8'){

                            row = row + 'অন্যান্য</br>';
                        }


                    }

                    row = row + '</td ></tr >';
                }

            }

        });
    }
});

