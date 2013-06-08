function [TrainingAccuracy,TestingAccuracy]=a_elm(TrainingData_File,TestingData_File,NumberofPart,NumberofHiddenNeurons,ActivationFunction)
%only for regression
%[TrainingAccuracy,TestingAccuracy]=a_elm(TrainingData_File,TestingData_File,NumberofPart,NumberofHiddenNeurons,ActivationFunction)

%Partition the TrainingData_File into N Parts:
partition2(TrainingData_File,NumberofPart);

%initialize
test_data=load(TestingData_File); %the raw file
train_data=load(TrainingData_File);

%the predicted result from d_elm_predict2
test_prediction=zeros(NumberofPart,size(test_data,1));
train_prediction=zeros(NumberofPart,size(train_data,1));

%The target of training and testing 
test_target=zeros(size(test_data,1),1);
train_target=zeros(size(train_data,1),1);

%Train N ELMs from N training data files, and each ELM will predict the

%all the results predicted by N ELM will be combined through average
for i=1:NumberofPart
   d_elm_train(cat(2,TrainingData_File,num2str(i)),0,NumberofHiddenNeurons,ActivationFunction);
   
   [train_output,train_target]=d_elm_predict2(TrainingData_File,TrainingData_File,i);
   train_prediction(i,:)=train_output;
   
   [test_output,test_target]=d_elm_predict2(TrainingData_File,TestingData_File,i);
   test_prediction(i,:)=test_output;
   
end

%Calculate the average of prediction data
for j=1:size(train_prediction,2)
    train_prediction(1,j)=mean(train_prediction(:,j));
end

for j=1:size(test_prediction,2)
    test_prediction(1,j)=mean(test_prediction(:,j));
end

TrainingAccuracy=sqrt(mse(train_target-train_prediction(1,:)));
TestingAccuracy=sqrt(mse(test_target-test_prediction(1,:)));


for i=1:NumberofPart
     delete(cat(2,TrainingData_File,num2str(i)));
     delete(cat(2,TrainingData_File,num2str(i),'_elm_model.mat')); 
end

end