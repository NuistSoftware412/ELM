function [TrainingTime,TestingTime,TrainingAccuracy,TestingAccuracy]=d_elm(TrainingData_File,TestingData_File,NumberofPart,Elm_Type,NumberofHiddenNeurons,ActivationFunction)
%Sample1: Regression
%[TrainingTime,TestingTime,TrainingAccuracy,TestingAccuracy]=d_elm('sinc_train','sinc_test',10,0,20,'sig')
%Sample2: Classification
%[TrainingTime,TestingTime,TrainingAccuracy,TestingAccuracy]=d_elm('sat_train','sat_test',10,1,20,'sig')
%Test1:[TrainingTime,TestingTime,TrainingAccuracy,TestingAccuracy]=d_elm('diabetes_train','diabetes_test',10,1,10,'sin')

%%%%%%%%%%% Macro definition
REGRESSION=0;
CLASSIFIER=1;

%Partition the TrainingData_File into N Parts:


%Training processing
if Elm_Type~=REGRESSION
    partition2(TrainingData_File,NumberofPart);
   [InputWeight,BiasofHiddenNeurons,OutputWeight,label]=d_elm_train(cat(2,TrainingData_File,num2str(1)),Elm_Type,NumberofHiddenNeurons,ActivationFunction);
      for i=2:NumberofPart
        [temp_InputWeight,temp_BiasofHiddenNeurons,temp_OutputWeight]=d_elm_train(cat(2,TrainingData_File,num2str(i)),Elm_Type,NumberofHiddenNeurons,ActivationFunction);
        %Update InputWeight :a
        InputWeight=cat(1,InputWeight,temp_InputWeight); 
        %Update BiasofHiddenNeurons :b
        BiasofHiddenNeurons=cat(1,BiasofHiddenNeurons,temp_BiasofHiddenNeurons);
        %Update OutputWeight :beta
        OutputWeight=cat(1,OutputWeight,temp_OutputWeight); 
    end

else 
    partition(TrainingData_File,NumberofPart);
   [InputWeight,BiasofHiddenNeurons,OutputWeight]=d_elm_train(cat(2,TrainingData_File,num2str(1)),Elm_Type,NumberofHiddenNeurons,ActivationFunction);
    [Output,Target]=d_elm_predict2(TrainingData_File,cat(2,TrainingData_File,num2str(NumberofPart+1)),1);
    Output=Output';
    for i=2:NumberofPart
        [temp_InputWeight,temp_BiasofHiddenNeurons,temp_OutputWeight]=d_elm_train(cat(2,TrainingData_File,num2str(i)),Elm_Type,NumberofHiddenNeurons,ActivationFunction);
        %Update InputWeight :a
        InputWeight=cat(1,InputWeight,temp_InputWeight); 
        %Update BiasofHiddenNeurons :b
        BiasofHiddenNeurons=cat(1,BiasofHiddenNeurons,temp_BiasofHiddenNeurons);
        %Update OutputWeight :beta
        OutputWeight=cat(1,OutputWeight,temp_OutputWeight);
        %Updata the output:P
        [temp_Output]=d_elm_predict2(TrainingData_File,cat(2,TrainingData_File,num2str(NumberofPart+1)),i);
        Output=cat(2,Output,temp_Output');
    end
    Omiga=pinv(Output)*Target';
    %Output2=Output*Omiga;
    %sqrt(mse(Output2-Target'))
    %Omiga
    ext_Omiga=Omiga(1,:);
    ind=ones(NumberofHiddenNeurons-1,size(Omiga,2));
    ext_Omiga=cat(1,ext_Omiga,ind);
    for k=2:NumberofHiddenNeurons
        ext_Omiga(k,:)=Omiga(1,:);
    end 
    for i=2:NumberofPart
        temp_Omiga=cat(1,Omiga(i,:),ind);
        for k=2:NumberofHiddenNeurons
            temp_Omiga(k,:)=Omiga(i,:);
        end
        ext_Omiga=cat(1,ext_Omiga,temp_Omiga);
    end
    OutputWeight=ext_Omiga.*OutputWeight; 
end


%Update OutputWeight
%size(ext_Omiga)
%size(OutputWeight)
%OutputWeight
%OutputWeight=OutputWeight/5;
if Elm_Type~=REGRESSION
    NumberofOutputNeurons=size(label,2);
    save('elm_model','NumberofOutputNeurons','InputWeight', 'BiasofHiddenNeurons', 'OutputWeight', 'ActivationFunction', 'label', 'Elm_Type');
else
    save('elm_model', 'InputWeight', 'BiasofHiddenNeurons', 'OutputWeight', 'ActivationFunction', 'Elm_Type');    
end

%NumberofInputNeurons

%Calculate the TrainingAccuracy.
[TrainingTime, TrainingAccuracy] = d_elm_predict(TrainingData_File);

%Testing processing and Calculate the TestingAccuracy.
[TestingTime, TestingAccuracy] = d_elm_predict(TestingData_File);


delete *_elm_model.mat
for i=1:NumberofPart+1
    delete(cat(2,TrainingData_File,num2str(i)))
end

end