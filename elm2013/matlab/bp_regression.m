function bp_regression(TrainingFile,TestingFile,HiddenNodeNumber)
%for regression only

train_data=load(TrainingFile);
x= train_data(:,2:size(train_data,2))';
size(x)
t= train_data(:,1)';
%train the single hidden layer network with BP
%[x,t]=simplefit_dataset;
net=feedforwardnet(HiddenNodeNumber);
net=train(net,x,t);
y=sim(net,x);
%perf=perform(net,t,y)
%test the training data


TrainingAccuracy=sqrt(mse(y-t))

%test the testing data
test_data = load(TestingFile);
x1= test_data(:,2:size(test_data,2))';
size(x1)
t1= test_data(:,1)';

y1=sim(net,x1);
%s=floor(s+0.5);

TestingAccuracy=sqrt(mse(y1-t1))

end
