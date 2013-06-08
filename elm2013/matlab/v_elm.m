function [TrainingAccuracy,TestingAccuracy]=v_elm(TrainingData_File,TestingData_File,NumberofPart,NumberofHiddenNeurons,ActivationFunction)

%only for classification
%[TrainingAccuracy,TestingAccuracy]=v_elm(TrainingData_File,TestingData_File,NumberofPart,NumberofHiddenNeurons,ActivationFunction)

%Partition the TrainingData_File into N Parts:
partition2(TrainingData_File,NumberofPart);

%initialize
test_data=load(TestingData_File);
train_data=load(TrainingData_File);
test_labels=zeros(NumberofPart,size(test_data,1));
train_labels=zeros(NumberofPart,size(train_data,1));
test_result=zeros(size(test_data,1),1);
train_result=zeros(size(train_data,1),1);
%Train N ELMs from N training data files, and each ELM will predict the
%test data file
%all the results predicted by N ELM will be combined through voting
for i=1:NumberofPart
   d_elm_train(cat(2,TrainingData_File,num2str(i)),1,NumberofHiddenNeurons,ActivationFunction);
   [labels]=d_elm_predict2(TrainingData_File,TrainingData_File,i);
   train_labels(i,:)=labels(:);
   [labels]=d_elm_predict2(TrainingData_File,TestingData_File,i);
   test_labels(i,:)=labels(:);
end


%find the label the accurs most
for j=1:size(train_data,1)
    [n,xout]=hist(train_labels(:,j),sort(train_labels(:,j)));
    tmp=xout(find(n==max(n)));
    train_result(j,1)=tmp(1);
end

for j=1:size(test_data,1)
    [n,xout]=hist(test_labels(:,j),sort(test_labels(:,j)));
    tmp=xout(find(n==max(n)));
    test_result(j,1)=tmp(1);
end


%count the rightly predicted
count=0;
for i=1:size(train_data,1)
    if train_result(i,1)==train_data(i,1)
           count=count+1;
    end
end
TrainingAccuracy = count/size(train_data,1);

count=0;
for i=1:size(test_data,1)
    if test_result(i,1)==test_data(i,1)
           count=count+1;
    end
end
TestingAccuracy = count/size(test_data,1);

%clear
delete *_elm_model.mat;
for i=1:NumberofPart
    delete(cat(2,TrainingData_File,num2str(i)))
end


end