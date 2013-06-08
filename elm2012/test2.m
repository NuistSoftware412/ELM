in=imread('before2.jpg');

P=zeros(size(in,1)*size(in,2),3);
k=1;
for i=1:size(in,1)
    for j=1:size(in,2)
        P(k,:)=in(i,j,:);
        k=k+1;;
    end
end